package xyz.nanian.owl.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.infra.minio.constant.MinioConstant;
import xyz.nanian.owl.infra.minio.service.FileStorageService;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.user.domain.dto.BlogPostCreateDTO;
import xyz.nanian.owl.user.domain.dto.BlogPostUpdateDTO;
import xyz.nanian.owl.user.domain.entity.BlogPostDO;
import xyz.nanian.owl.user.domain.vo.BlogPostVO;
import xyz.nanian.owl.user.mapper.BlogPostMapper;
import xyz.nanian.owl.user.mapstruct.BlogPostConvert;
import xyz.nanian.owl.user.service.BlogPostService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogPostServiceImpl implements BlogPostService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final BlogPostMapper blogPostMapper;
    private final BlogPostConvert blogPostConvert;
    private final FileStorageService fileStorageService;

    @Override
    public List<BlogPostVO> listByUserId(Long userId) {
        List<BlogPostDO> list = blogPostMapper.selectList(
                Wrappers.<BlogPostDO>lambdaQuery()
                        .eq(BlogPostDO::getUserId, userId)
                        .orderByDesc(BlogPostDO::getSortOrder)
                        .orderByDesc(BlogPostDO::getCreateTime));
        return toVOList(list);
    }

    @Override
    public ResultPage<BlogPostVO> pageByUserId(Long userId, long pageNum, long pageSize) {
        pageNum = normalizePageNum(pageNum);
        pageSize = normalizePageSize(pageSize);

        Page<BlogPostDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BlogPostDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BlogPostDO::getUserId, userId)
                .orderByDesc(BlogPostDO::getSortOrder)
                .orderByDesc(BlogPostDO::getCreateTime);

        return buildResultPage(blogPostMapper.selectPage(page, wrapper));
    }

    @Override
    public ResultPage<BlogPostVO> pageAll(long pageNum, long pageSize) {
        pageNum = normalizePageNum(pageNum);
        pageSize = normalizePageSize(pageSize);

        Page<BlogPostDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<BlogPostDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(BlogPostDO::getStatus, 1)
                .orderByDesc(BlogPostDO::getSortOrder)
                .orderByDesc(BlogPostDO::getCreateTime);

        return buildResultPage(blogPostMapper.selectPage(page, wrapper));
    }

    @Override
    public BlogPostVO getById(Long id, Long userId) {
        BlogPostDO post = blogPostMapper.selectById(id);
        if (post == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        if (post.getStatus() == 0 && !post.getUserId().equals(userId)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return toVO(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "博客", action = "创建文章", persist = true)
    public Long create(BlogPostCreateDTO dto, Long userId) {
        LocalDateTime now = LocalDateTime.now();
        BlogPostDO post = new BlogPostDO();
        post.setUserId(userId);
        post.setTitle(dto.getTitle().trim());
        post.setExcerpt(dto.getExcerpt());
        post.setContent(dto.getContent());
        post.setTags(dto.getTags());
        post.setStatus(0);
        post.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        post.setCreateTime(now);
        post.setUpdateTime(now);

        if (dto.getCover() != null && !dto.getCover().isEmpty()) {
            String coverUrl = fileStorageService.upload(dto.getCover(), MinioConstant.BUCKET_IMAGES);
            post.setCoverUrl(coverUrl);
        }

        blogPostMapper.insert(post);
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "博客", action = "更新文章", persist = true)
    public Boolean update(Long id, BlogPostUpdateDTO dto, Long userId) {
        BlogPostDO post = blogPostMapper.selectById(id);
        if (post == null || !post.getUserId().equals(userId)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }

        LambdaUpdateWrapper<BlogPostDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogPostDO::getId, id);
        boolean changed = false;

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            wrapper.set(BlogPostDO::getTitle, dto.getTitle().trim());
            changed = true;
        }
        if (dto.getExcerpt() != null) {
            wrapper.set(BlogPostDO::getExcerpt, dto.getExcerpt());
            changed = true;
        }
        if (dto.getContent() != null) {
            wrapper.set(BlogPostDO::getContent, dto.getContent());
            changed = true;
        }
        if (dto.getTags() != null) {
            wrapper.set(BlogPostDO::getTags, dto.getTags());
            changed = true;
        }
        if (dto.getSortOrder() != null) {
            wrapper.set(BlogPostDO::getSortOrder, dto.getSortOrder());
            changed = true;
        }
        if (dto.getCover() != null && !dto.getCover().isEmpty()) {
            String coverUrl = fileStorageService.upload(dto.getCover(), MinioConstant.BUCKET_IMAGES);
            wrapper.set(BlogPostDO::getCoverUrl, coverUrl);
            changed = true;
        }

        if (changed) {
            blogPostMapper.update(null, wrapper);
        }
        return true;
    }

    @Override
    @OperationLog(module = "博客", action = "删除文章", persist = true)
    public Boolean deleteById(Long id, Long userId) {
        BlogPostDO post = blogPostMapper.selectById(id);
        if (post == null || !post.getUserId().equals(userId)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return blogPostMapper.deleteById(id) > 0;
    }

    // ------------------------------------------------------------
    // 私有辅助方法
    // ------------------------------------------------------------

    private BlogPostVO toVO(BlogPostDO post) {
        BlogPostVO vo = blogPostConvert.toVO(post);
        vo.setTags(parseTags(post.getTags()));
        return vo;
    }

    private List<BlogPostVO> toVOList(List<BlogPostDO> posts) {
        return posts.stream().map(this::toVO).toList();
    }

    private List<String> parseTags(String tagsJson) {
        if (tagsJson == null || tagsJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return OBJECT_MAPPER.readValue(tagsJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    private ResultPage<BlogPostVO> buildResultPage(IPage<BlogPostDO> result) {
        ResultPage<BlogPostVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(toVOList(result.getRecords()));
        return pageResult;
    }

    private long normalizePageNum(long pageNum) {
        return pageNum <= 0 ? 1 : pageNum;
    }

    private long normalizePageSize(long pageSize) {
        if (pageSize <= 0) {
            return DEFAULT_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }
}