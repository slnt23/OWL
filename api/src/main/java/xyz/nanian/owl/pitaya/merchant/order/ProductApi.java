package xyz.nanian.owl.pitaya.merchant.order;


import xyz.nanian.owl.pitaya.consumer.dto.ProductDTO;

/**
 * 商品管理
 *
 * @author slnt23
 * @since 2026/1/6
 */

public interface ProductApi {

    /**
     * 商品新增
     * @param productDTO
     */
    void addProduct(ProductDTO productDTO);

    /**
     * 修改商品
     * @param productDTO
     */
    void updateProduct(ProductDTO productDTO);

    /**
     * 删除商品
     * @param productDTO
     */
    void deleteProduct(ProductDTO productDTO);

    /**
     * 上下架管理
     * @param productDTO
     */
    void updateProductStatus(ProductDTO productDTO);


}
