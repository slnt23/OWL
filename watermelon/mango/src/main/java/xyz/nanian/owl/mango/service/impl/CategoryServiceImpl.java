package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.CategoryDTO;
import xyz.nanian.owl.mango.domain.entity.BlogCategoryDO;
import xyz.nanian.owl.mango.domain.entity.BlogPostDO;
import xyz.nanian.owl.mango.domain.vo.CategoryVO;
import xyz.nanian.owl.mango.mapper.BlogCategoryMapper;
import xyz.nanian.owl.mango.mapper.BlogPostMapper;
import xyz.nanian.owl.mango.mapstruct.CategoryConvert;
import xyz.nanian.owl.mango.service.CategoryService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客分类服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service("mangoCategoryServiceImpl")
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final BlogCategoryMapper blogCategoryMapper;
    private final BlogPostMapper blogPostMapper;
    private final CategoryConvert categoryConvert;

    @Override
    public List<CategoryVO> list() {
        List<BlogCategoryDO> categories = blogCategoryMapper.selectList(Wrappers.<BlogCategoryDO>lambdaQuery()
                .orderByAsc(BlogCategoryDO::getSortOrder)
                .orderByAsc(BlogCategoryDO::getId));
        List<CategoryVO> vos = categoryConvert.toVOList(categories);
        vos.forEach(vo -> vo.setPostCount(countPublishedByCategory(vo.getId())));
        return vos;
    }

    @Override
    @OperationLog(module = "博客", action = "创建分类", persist = true)
    public Long create(CategoryDTO dto) {
        String name = dto.getName().trim();
        ensureNameUnique(name, null);

        LocalDateTime now = LocalDateTime.now();
        BlogCategoryDO category = new BlogCategoryDO();
        category.setName(name);
        category.setSlug(dto.getSlug());
        category.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        category.setCreateTime(now);
        category.setUpdateTime(now);
        blogCategoryMapper.insert(category);
        return category.getId();
    }

    @Override
    @OperationLog(module = "博客", action = "更新分类", persist = true)
    public Boolean update(Long id, CategoryDTO dto) {
        requireCategory(id);

        LambdaUpdateWrapper<BlogCategoryDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogCategoryDO::getId, id);
        boolean changed = false;
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String name = dto.getName().trim();
            ensureNameUnique(name, id);
            wrapper.set(BlogCategoryDO::getName, name);
            changed = true;
        }
        if (dto.getSlug() != null) {
            wrapper.set(BlogCategoryDO::getSlug, dto.getSlug());
            changed = true;
        }
        if (dto.getSortOrder() != null) {
            wrapper.set(BlogCategoryDO::getSortOrder, dto.getSortOrder());
            changed = true;
        }
        return !changed || blogCategoryMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "删除分类", persist = true)
    public Boolean deleteById(Long id) {
        requireCategory(id);
        // 文章 category_id 由数据库 ON DELETE SET NULL 置空
        return blogCategoryMapper.deleteById(id) > 0;
    }

    private BlogCategoryDO requireCategory(Long id) {
        BlogCategoryDO category = blogCategoryMapper.selectById(id);
        if (category == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return category;
    }

    private void ensureNameUnique(String name, Long excludeId) {
        LambdaQueryWrapper<BlogCategoryDO> wrapper = Wrappers.<BlogCategoryDO>lambdaQuery()
                .eq(BlogCategoryDO::getName, name);
        if (excludeId != null) {
            wrapper.ne(BlogCategoryDO::getId, excludeId);
        }
        if (blogCategoryMapper.selectCount(wrapper) > 0) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }
    }

    private int countPublishedByCategory(Long categoryId) {
        return Math.toIntExact(blogPostMapper.selectCount(Wrappers.<BlogPostDO>lambdaQuery()
                .eq(BlogPostDO::getCategoryId, categoryId)
                .eq(BlogPostDO::getIsPublished, 1)));
    }
}
