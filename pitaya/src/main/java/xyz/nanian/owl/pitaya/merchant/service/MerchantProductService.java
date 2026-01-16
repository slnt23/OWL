package xyz.nanian.owl.pitaya.merchant.service;


import xyz.nanian.owl.pitaya.dto.ProductDTO;

/**
 * 商家商品service
 *
 * @author slnt23
 * @since 2026/1/15
 */

public interface MerchantProductService {

    /**
     * 新增商品
     * @param productDTO DTO
     * @return 0/1
     */
    Boolean saveProduct(ProductDTO productDTO);

    /**
     * 更新商品
     * @param productDTO DTO
     * @return 0/1
     */
    Boolean updateProduct(ProductDTO productDTO);

    Boolean removeProduct(Integer productId);

    Boolean updateProductStatus(Integer productId, String status);
}
