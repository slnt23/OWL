package xyz.nanian.owl.pitaya.cart;


import xyz.nanian.owl.pitaya.dto.ProductDTO;

/**
 * 购物车模块Api
 *
 * @author slnt23
 * @since 2025/11/18
 */

public interface CartApi {

    /**
     * 新增购物车中商品
     * @param productDTO
     */
    void addProduct(ProductDTO productDTO);

    /**
     * 删除购物车中商品
     * @param productDTO
     */
    void removeProduct(ProductDTO productDTO);

    /**
     * 修改购物车中的商品
     * @param productDTO
     */
    void updateProduct(ProductDTO productDTO);

    /**
     * 购物车列表展示
     * @param sellerId
     */
    void queryProduct(String sellerId);
}
