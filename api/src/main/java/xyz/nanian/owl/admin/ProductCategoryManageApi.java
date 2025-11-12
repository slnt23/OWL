package xyz.nanian.owl.admin;


import xyz.nanian.owl.pitaya.dto.ProductCategoryDTO;

/**
 * 商品分类管理接口
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface ProductCategoryManageApi {

    void addProductCategory(ProductCategoryDTO productCategoryDTO);

    void updateProductCategory(ProductCategoryDTO productCategoryDTO);

    void deleteProductCategory(ProductCategoryDTO productCategoryDTO);
}
