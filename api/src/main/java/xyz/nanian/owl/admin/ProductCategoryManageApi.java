package xyz.nanian.owl.admin;


import xyz.nanian.owl.pitaya.dto.ProductCategoryDTO;

/**
 * 商品分类管理
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface ProductCategoryManageApi {

    /**
     * 新增商品分类管理
     * @param productCategoryDTO
     */
    Integer addProductCategory(ProductCategoryDTO productCategoryDTO);

    /**
     * 更新商品分类
     * @param productCategoryDTO
     */
    void updateProductCategory(ProductCategoryDTO productCategoryDTO);

    /**
     * 删除商品分类
     * @param productCategoryDTO
     */
    void deleteProductCategory(ProductCategoryDTO productCategoryDTO);
}
