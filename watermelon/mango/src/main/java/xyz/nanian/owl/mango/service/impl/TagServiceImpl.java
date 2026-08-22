package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.TagDTO;
import xyz.nanian.owl.mango.domain.entity.BlogPostDO;
import xyz.nanian.owl.mango.domain.entity.BlogTagDO;
import xyz.nanian.owl.mango.domain.vo.TagVO;
import xyz.nanian.owl.mango.mapper.BlogPostMapper;
import xyz.nanian.owl.mango.mapper.BlogTagMapper;
import xyz.nanian.owl.mango.mapstruct.TagConvert;
import xyz.nanian.owl.mango.service.TagService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客标签服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final BlogTagMapper blogTagMapper;
    private final BlogPostMapper blogPostMapper;
    private final TagConvert tagConvert;

    @Override
    public List<TagVO> list() {
        List<BlogTagDO> tags = blogTagMapper.selectList(Wrappers.<BlogTagDO>lambdaQuery()
                .orderByAsc(BlogTagDO::getId));
        List<TagVO> vos = tagConvert.toVOList(tags);
        vos.forEach(vo -> vo.setPostCount(countPublishedByTag(vo.getId())));
        return vos;
    }

    @Override
    @OperationLog(module = "博客", action = "创建标签", persist = true)
    public Long create(TagDTO dto) {
        String name = dto.getName().trim();
        ensureNameUnique(name, null);

        LocalDateTime now = LocalDateTime.now();
        BlogTagDO tag = new BlogTagDO();
        tag.setName(name);
        tag.setSlug(dto.getSlug());
        tag.setCreateTime(now);
        tag.setUpdateTime(now);
        blogTagMapper.insert(tag);
        return tag.getId();
    }

    @Override
    @OperationLog(module = "博客", action = "更新标签", persist = true)
    public Boolean update(Long id, TagDTO dto) {
        requireTag(id);

        LambdaUpdateWrapper<BlogTagDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogTagDO::getId, id);
        boolean changed = false;
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String name = dto.getName().trim();
            ensureNameUnique(name, id);
            wrapper.set(BlogTagDO::getName, name);
            changed = true;
        }
        if (dto.getSlug() != null) {
            wrapper.set(BlogTagDO::getSlug, dto.getSlug());
            changed = true;
        }
        return !changed || blogTagMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "删除标签", persist = true)
    public Boolean deleteById(Long id) {
        requireTag(id);
        // blog_post_tag 关联关系由数据库 ON DELETE CASCADE 一并清除
        return blogTagMapper.deleteById(id) > 0;
    }

    private BlogTagDO requireTag(Long id) {
        BlogTagDO tag = blogTagMapper.selectById(id);
        if (tag == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return tag;
    }

    private void ensureNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<BlogTagDO> wrapper = Wrappers.<BlogTagDO>lambdaQuery()
                .eq(BlogTagDO::getName, name);
        if (excludeId != null) {
            wrapper.ne(BlogTagDO::getId, excludeId);
        }
        if (blogTagMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }
    }

    private int countPublishedByTag(Long tagId) {
        return Math.toIntExact(blogPostMapper.selectCount(Wrappers.<BlogPostDO>lambdaQuery()
                .apply("id in (select post_id from blog_post_tag where tag_id = {0})", tagId)
                .eq(BlogPostDO::getIsPublished, 1)));
    }
}
