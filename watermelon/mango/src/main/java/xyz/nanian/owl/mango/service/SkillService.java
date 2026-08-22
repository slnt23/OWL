package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.mango.domain.dto.SkillCategoryDTO;
import xyz.nanian.owl.mango.domain.dto.SkillItemDTO;
import xyz.nanian.owl.mango.domain.vo.SkillCategoryVO;

import java.util.List;

/**
 * 博客技能服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface SkillService {

    /**
     * 全部技能分类（含条目）
     *
     * @return 技能分类列表
     */
    List<SkillCategoryVO> list();

    /**
     * 新增技能分类
     *
     * @param dto 入参
     * @return 新分类 ID
     */
    Long createCategory(SkillCategoryDTO dto);

    /**
     * 更新技能分类
     *
     * @param id  分类 ID
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean updateCategory(Long id, SkillCategoryDTO dto);

    /**
     * 删除技能分类（级联删除条目）
     *
     * @param id 分类 ID
     * @return 是否删除成功
     */
    Boolean deleteCategory(Long id);

    /**
     * 新增技能条目
     *
     * @param dto 入参
     * @return 新条目 ID
     */
    Long createItem(SkillItemDTO dto);

    /**
     * 更新技能条目
     *
     * @param id  条目 ID
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean updateItem(Long id, SkillItemDTO dto);

    /**
     * 删除技能条目
     *
     * @param id 条目 ID
     * @return 是否删除成功
     */
    Boolean deleteItem(Long id);
}
