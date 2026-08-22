package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.mango.domain.dto.CategoryDTO;
import xyz.nanian.owl.mango.domain.vo.CategoryVO;

import java.util.List;

/**
 * 博客分类服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface CategoryService {

    /**
     * 全部分类（含已发布文章数）
     *
     * @return 分类列表
     */
    List<CategoryVO> list();

    /**
     * 创建分类
     *
     * @param dto 入参
     * @return 新分类 ID
     */
    Long create(CategoryDTO dto);

    /**
     * 更新分类
     *
     * @param id  分类 ID
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean update(Long id, CategoryDTO dto);

    /**
     * 删除分类（文章分类置空）
     *
     * @param id 分类 ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);
}
