package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.SkillCategoryDTO;
import xyz.nanian.owl.mango.domain.dto.SkillItemDTO;
import xyz.nanian.owl.mango.domain.entity.BlogSkillCategoryDO;
import xyz.nanian.owl.mango.domain.entity.BlogSkillItemDO;
import xyz.nanian.owl.mango.domain.vo.SkillCategoryVO;
import xyz.nanian.owl.mango.mapper.BlogSkillCategoryMapper;
import xyz.nanian.owl.mango.mapper.BlogSkillItemMapper;
import xyz.nanian.owl.mango.mapstruct.SkillConvert;
import xyz.nanian.owl.mango.service.SkillService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客技能服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final BlogSkillCategoryMapper blogSkillCategoryMapper;
    private final BlogSkillItemMapper blogSkillItemMapper;
    private final SkillConvert skillConvert;

    @Override
    public List<SkillCategoryVO> list() {
        List<BlogSkillCategoryDO> categories = blogSkillCategoryMapper.selectList(
                Wrappers.<BlogSkillCategoryDO>lambdaQuery()
                        .orderByAsc(BlogSkillCategoryDO::getSortOrder)
                        .orderByAsc(BlogSkillCategoryDO::getId));
        return categories.stream().map(category -> {
            SkillCategoryVO vo = skillConvert.categoryToVO(category);
            List<BlogSkillItemDO> items = blogSkillItemMapper.selectList(
                    Wrappers.<BlogSkillItemDO>lambdaQuery()
                            .eq(BlogSkillItemDO::getCategoryId, category.getId())
                            .orderByAsc(BlogSkillItemDO::getSortOrder)
                            .orderByAsc(BlogSkillItemDO::getId));
            vo.setItems(skillConvert.itemListToVOList(items));
            return vo;
        }).toList();
    }

    @Override
    @OperationLog(module = "博客", action = "新增技能分类", persist = true)
    public Long createCategory(SkillCategoryDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        BlogSkillCategoryDO category = new BlogSkillCategoryDO();
        category.setCategory(dto.getCategory().trim());
        category.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        category.setCreateTime(now);
        category.setUpdateTime(now);
        blogSkillCategoryMapper.insert(category);
        return category.getId();
    }

    @Override
    @OperationLog(module = "博客", action = "更新技能分类", persist = true)
    public Boolean updateCategory(Long id, SkillCategoryDTO dto) {
        requireCategory(id);

        LambdaUpdateWrapper<BlogSkillCategoryDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogSkillCategoryDO::getId, id);
        boolean changed = false;
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) {
            wrapper.set(BlogSkillCategoryDO::getCategory, dto.getCategory().trim());
            changed = true;
        }
        if (dto.getSortOrder() != null) {
            wrapper.set(BlogSkillCategoryDO::getSortOrder, dto.getSortOrder());
            changed = true;
        }
        return !changed || blogSkillCategoryMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "删除技能分类", persist = true)
    public Boolean deleteCategory(Long id) {
        requireCategory(id);
        // blog_skill_item 由数据库 ON DELETE CASCADE 一并删除
        return blogSkillCategoryMapper.deleteById(id) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "新增技能条目", persist = true)
    public Long createItem(SkillItemDTO dto) {
        requireCategory(dto.getCategoryId());

        LocalDateTime now = LocalDateTime.now();
        BlogSkillItemDO item = new BlogSkillItemDO();
        item.setCategoryId(dto.getCategoryId());
        item.setName(dto.getName().trim());
        item.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        item.setCreateTime(now);
        item.setUpdateTime(now);
        blogSkillItemMapper.insert(item);
        return item.getId();
    }

    @Override
    @OperationLog(module = "博客", action = "更新技能条目", persist = true)
    public Boolean updateItem(Long id, SkillItemDTO dto) {
        requireItem(id);

        LambdaUpdateWrapper<BlogSkillItemDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogSkillItemDO::getId, id);
        boolean changed = false;
        if (dto.getCategoryId() != null) {
            requireCategory(dto.getCategoryId());
            wrapper.set(BlogSkillItemDO::getCategoryId, dto.getCategoryId());
            changed = true;
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            wrapper.set(BlogSkillItemDO::getName, dto.getName().trim());
            changed = true;
        }
        if (dto.getSortOrder() != null) {
            wrapper.set(BlogSkillItemDO::getSortOrder, dto.getSortOrder());
            changed = true;
        }
        return !changed || blogSkillItemMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "删除技能条目", persist = true)
    public Boolean deleteItem(Long id) {
        requireItem(id);
        return blogSkillItemMapper.deleteById(id) > 0;
    }

    private BlogSkillCategoryDO requireCategory(Long id) {
        BlogSkillCategoryDO category = blogSkillCategoryMapper.selectById(id);
        if (category == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return category;
    }

    private BlogSkillItemDO requireItem(Long id) {
        BlogSkillItemDO item = blogSkillItemMapper.selectById(id);
        if (item == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return item;
    }
}
