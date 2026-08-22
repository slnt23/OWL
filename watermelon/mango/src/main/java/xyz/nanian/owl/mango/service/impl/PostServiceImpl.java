package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.PostCreateDTO;
import xyz.nanian.owl.mango.domain.dto.PostQueryDTO;
import xyz.nanian.owl.mango.domain.dto.PostUpdateDTO;
import xyz.nanian.owl.mango.domain.entity.BlogCategoryDO;
import xyz.nanian.owl.mango.domain.entity.BlogPostDO;
import xyz.nanian.owl.mango.domain.entity.BlogPostTagDO;
import xyz.nanian.owl.mango.domain.entity.BlogTagDO;
import xyz.nanian.owl.mango.domain.vo.PostDetailVO;
import xyz.nanian.owl.mango.domain.vo.PostVO;
import xyz.nanian.owl.mango.domain.vo.TagVO;
import xyz.nanian.owl.mango.mapper.BlogCategoryMapper;
import xyz.nanian.owl.mango.mapper.BlogPostMapper;
import xyz.nanian.owl.mango.mapper.BlogPostTagMapper;
import xyz.nanian.owl.mango.mapper.BlogTagMapper;
import xyz.nanian.owl.mango.mapstruct.PostConvert;
import xyz.nanian.owl.mango.mapstruct.TagConvert;
import xyz.nanian.owl.mango.service.PostService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 博客文章服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private static final int MAX_PAGE_SIZE = 50;
    private static final int DEFAULT_PAGE_SIZE = 8;

    private final BlogPostMapper blogPostMapper;
    private final BlogPostTagMapper blogPostTagMapper;
    private final BlogCategoryMapper blogCategoryMapper;
    private final BlogTagMapper blogTagMapper;
    private final PostConvert postConvert;
    private final TagConvert tagConvert;

    @Override
    public ResultPage<PostVO> page(PostQueryDTO query) {
        int pageNum = query.getPageNum() == null || query.getPageNum() <= 0 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() <= 0
                ? DEFAULT_PAGE_SIZE
                : Math.min(query.getPageSize(), MAX_PAGE_SIZE);

        LambdaQueryWrapper<BlogPostDO> wrapper = Wrappers.lambdaQuery();
        if (query.getCategoryId() != null) {
            wrapper.eq(BlogPostDO::getCategoryId, query.getCategoryId());
        }
        if (query.getTagId() != null) {
            wrapper.apply("id in (select post_id from blog_post_tag where tag_id = {0})", query.getTagId());
        }
        if (query.getKeyword() != null && !query.getKeyword().isBlank()) {
            String keyword = query.getKeyword().trim();
            wrapper.and(w -> w.like(BlogPostDO::getTitle, keyword).or().like(BlogPostDO::getExcerpt, keyword));
        }
        if (query.getLang() != null && !query.getLang().isBlank()) {
            wrapper.eq(BlogPostDO::getLang, query.getLang().trim());
        }
        boolean includeDraft = Boolean.TRUE.equals(query.getIncludeDraft())
                && CurrentUserContext.getUserId() != null;
        if (!includeDraft) {
            wrapper.eq(BlogPostDO::getIsPublished, 1);
        }
        wrapper.orderByDesc(BlogPostDO::getIsTop)
                .orderByDesc(BlogPostDO::getPublishTime)
                .orderByDesc(BlogPostDO::getCreateTime);

        Page<BlogPostDO> page = new Page<>(pageNum, pageSize);
        IPage<BlogPostDO> result = blogPostMapper.selectPage(page, wrapper);

        List<PostVO> records = postConvert.toVOList(result.getRecords());
        fillCategoryName(records);
        fillTags(records);

        ResultPage<PostVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public PostDetailVO getById(Long id) {
        BlogPostDO post = requireVisiblePost(blogPostMapper.selectById(id));
        return buildDetail(post);
    }

    @Override
    public PostDetailVO getBySlug(String slug) {
        BlogPostDO post = requireVisiblePost(blogPostMapper.selectOne(
                Wrappers.<BlogPostDO>lambdaQuery().eq(BlogPostDO::getSlug, slug)));
        return buildDetail(post);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "博客", action = "创建文章", persist = true)
    public Long create(PostCreateDTO dto) {
        Long userId = CurrentUserContext.getUserId();
        if (userId == null) {
            throw new BizException(ResultStatus.UNAUTHORIZED);
        }

        String slug = dto.getSlug() == null || dto.getSlug().isBlank()
                ? generateUniqueSlug(dto.getTitle())
                : ensureUniqueSlug(dto.getSlug().trim(), null);

        LocalDateTime now = LocalDateTime.now();
        BlogPostDO post = new BlogPostDO();
        post.setTitle(dto.getTitle().trim());
        post.setExcerpt(dto.getExcerpt());
        post.setContent(dto.getContent());
        post.setCoverImage(dto.getCoverImage());
        post.setSlug(slug);
        post.setCategoryId(dto.getCategoryId());
        post.setLang(dto.getLang() == null || dto.getLang().isBlank() ? "zh" : dto.getLang().trim());
        post.setReadTime(dto.getReadTime() == null ? estimateReadTime(dto.getContent()) : dto.getReadTime());
        post.setIsPublished(Boolean.TRUE.equals(dto.getIsPublished()) ? 1 : 0);
        post.setIsTop(Boolean.TRUE.equals(dto.getIsTop()) ? 1 : 0);
        post.setViewCount(0);
        post.setLikeCount(0);
        post.setPublishTime(resolvePublishTime(dto.getPublishTime(), Boolean.TRUE.equals(dto.getIsPublished()), now));
        post.setCreatedBy(userId);
        post.setCreateTime(now);
        post.setUpdateTime(now);

        blogPostMapper.insert(post);

        replaceTags(post.getId(), dto.getTagIds());
        return post.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "博客", action = "更新文章", persist = true)
    public Boolean update(Long id, PostUpdateDTO dto) {
        BlogPostDO post = requirePost(id);

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
            if (dto.getReadTime() == null) {
                wrapper.set(BlogPostDO::getReadTime, estimateReadTime(dto.getContent()));
            }
            changed = true;
        }
        if (dto.getReadTime() != null) {
            wrapper.set(BlogPostDO::getReadTime, dto.getReadTime());
            changed = true;
        }
        if (dto.getCoverImage() != null) {
            wrapper.set(BlogPostDO::getCoverImage, dto.getCoverImage());
            changed = true;
        }
        if (dto.getSlug() != null && !dto.getSlug().isBlank()) {
            wrapper.set(BlogPostDO::getSlug, ensureUniqueSlug(dto.getSlug().trim(), id));
            changed = true;
        }
        if (dto.getCategoryId() != null) {
            wrapper.set(BlogPostDO::getCategoryId, dto.getCategoryId());
            changed = true;
        }
        if (dto.getLang() != null && !dto.getLang().isBlank()) {
            wrapper.set(BlogPostDO::getLang, dto.getLang().trim());
            changed = true;
        }
        if (dto.getIsPublished() != null) {
            wrapper.set(BlogPostDO::getIsPublished, Boolean.TRUE.equals(dto.getIsPublished()) ? 1 : 0);
            changed = true;
        }
        if (dto.getIsTop() != null) {
            wrapper.set(BlogPostDO::getIsTop, Boolean.TRUE.equals(dto.getIsTop()) ? 1 : 0);
            changed = true;
        }
        if (dto.getPublishTime() != null) {
            wrapper.set(BlogPostDO::getPublishTime, dto.getPublishTime());
            changed = true;
        }
        if (changed) {
            blogPostMapper.update(null, wrapper);
        }
        if (dto.getTagIds() != null) {
            replaceTags(id, dto.getTagIds());
        }
        return true;
    }

    @Override
    @OperationLog(module = "博客", action = "删除文章", persist = true)
    public Boolean deleteById(Long id) {
        requirePost(id);
        // blog_post_tag 由数据库 ON DELETE CASCADE 一并清除
        return blogPostMapper.deleteById(id) > 0;
    }

    // ------------------------------------------------------------
    // 私有辅助方法
    // ------------------------------------------------------------

    private BlogPostDO requirePost(Long id) {
        BlogPostDO post = blogPostMapper.selectById(id);
        if (post == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return post;
    }

    /**
     * 草稿对未登录访客不可见
     */
    private BlogPostDO requireVisiblePost(BlogPostDO post) {
        if (post == null || (post.getIsPublished() == 0 && CurrentUserContext.getUserId() == null)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return post;
    }

    private PostDetailVO buildDetail(BlogPostDO post) {
        // 浏览量原子 +1，不因详情重复访问重复计数以外的问题做幂等处理
        blogPostMapper.update(null, new LambdaUpdateWrapper<BlogPostDO>()
                .eq(BlogPostDO::getId, post.getId())
                .setSql("view_count = view_count + 1"));

        post.setViewCount(post.getViewCount() == null ? 1 : post.getViewCount() + 1);
        PostDetailVO detail = postConvert.toDetailVO(post);
        fillCategoryName(Collections.singletonList(detail));
        fillTags(Collections.singletonList(detail));
        return detail;
    }

    private void fillCategoryName(List<? extends PostVO> records) {
        Set<Long> categoryIds = records.stream()
                .map(PostVO::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (categoryIds.isEmpty()) {
            return;
        }
        Map<Long, String> idToName = blogCategoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(BlogCategoryDO::getId, BlogCategoryDO::getName, (a, b) -> a));
        records.forEach(vo -> vo.setCategoryName(idToName.get(vo.getCategoryId())));
    }

    private void fillTags(List<? extends PostVO> records) {
        if (records.isEmpty()) {
            return;
        }
        List<Long> postIds = records.stream().map(PostVO::getId).collect(Collectors.toList());
        List<BlogPostTagDO> relations = blogPostTagMapper.selectList(
                Wrappers.<BlogPostTagDO>lambdaQuery().in(BlogPostTagDO::getPostId, postIds));
        if (relations.isEmpty()) {
            records.forEach(vo -> vo.setTags(Collections.emptyList()));
            return;
        }
        Map<Long, List<Long>> postIdToTagIds = relations.stream().collect(
                Collectors.groupingBy(BlogPostTagDO::getPostId,
                        Collectors.mapping(BlogPostTagDO::getTagId, Collectors.toList())));
        Set<Long> tagIds = relations.stream().map(BlogPostTagDO::getTagId).collect(Collectors.toSet());
        Map<Long, BlogTagDO> tagMap = blogTagMapper.selectBatchIds(tagIds).stream()
                .collect(Collectors.toMap(BlogTagDO::getId, t -> t, (a, b) -> a));

        records.forEach(vo -> {
            List<Long> ids = postIdToTagIds.getOrDefault(vo.getId(), Collections.emptyList());
            List<TagVO> tags = new ArrayList<>();
            for (Long tagId : ids) {
                BlogTagDO tag = tagMap.get(tagId);
                if (tag != null) {
                    tags.add(tagConvert.toVO(tag));
                }
            }
            vo.setTags(tags);
        });
    }

    private void replaceTags(Long postId, List<Long> tagIds) {
        blogPostTagMapper.delete(Wrappers.<BlogPostTagDO>lambdaQuery()
                .eq(BlogPostTagDO::getPostId, postId));
        if (tagIds == null || tagIds.isEmpty()) {
            return;
        }
        List<Long> distinctIds = tagIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (!distinctIds.isEmpty()) {
            long exists = blogTagMapper.selectCount(Wrappers.<BlogTagDO>lambdaQuery()
                    .in(BlogTagDO::getId, distinctIds));
            if (exists != distinctIds.size()) {
                throw new BizException(ResultStatus.DATA_NOT_EXIST);
            }
        }
        for (Long tagId : distinctIds) {
            BlogPostTagDO relation = new BlogPostTagDO();
            relation.setPostId(postId);
            relation.setTagId(tagId);
            blogPostTagMapper.insert(relation);
        }
    }

    private String generateUniqueSlug(String title) {
        String base = toKebabCase(title);
        String candidate = base;
        int i = 1;
        while (slugExists(candidate)) {
            candidate = base + "-" + (i++);
        }
        return candidate;
    }

    private String ensureUniqueSlug(String slug, Long excludeId) {
        LambdaQueryWrapper<BlogPostDO> wrapper = Wrappers.<BlogPostDO>lambdaQuery()
                .eq(BlogPostDO::getSlug, slug);
        if (excludeId != null) {
            wrapper.ne(BlogPostDO::getId, excludeId);
        }
        if (blogPostMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }
        return slug;
    }

    private boolean slugExists(String slug) {
        return blogPostMapper.selectCount(
                Wrappers.<BlogPostDO>lambdaQuery().eq(BlogPostDO::getSlug, slug)) > 0;
    }

    /**
     * 英文标题转 kebab-case，中文标题兜底为 post
     */
    private String toKebabCase(String title) {
        if (title == null || title.isBlank()) {
            return "post";
        }
        String base = title.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("[\\s-]+", "-")
                .replaceAll("^-+|-+$", "");
        return base.isBlank() ? "post" : base;
    }

    private LocalDateTime resolvePublishTime(LocalDateTime publishTime, boolean isPublished, LocalDateTime now) {
        if (publishTime != null) {
            return publishTime;
        }
        return isPublished ? now : null;
    }

    /**
     * 按正文字数估算阅读时长：中文约 300 字/分钟，英文约 200 词/分钟
     */
    private Integer estimateReadTime(String content) {
        if (content == null || content.isBlank()) {
            return 1;
        }
        long cn = content.chars().filter(c -> c > 127).count();
        String english = content.replaceAll("[^A-Za-z]+", " ").trim();
        long words = english.isEmpty() ? 0 : english.split("\\s+").length;
        int minutes = (int) Math.ceil(cn / 300.0 + words / 200.0);
        return Math.max(1, minutes);
    }
}
