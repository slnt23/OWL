package xyz.nanian.owl.pitaya.merchant.service;


import xyz.nanian.owl.pitaya.domain.dto.ProductDTO;

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

    /**
     * 删除商品
     * @param productId
     * @return
     */
    Boolean removeProduct(Long productId);

    /**
     * 更新上下架
     * @param productId
     * @param status
     * @return
     */
    Boolean updateProductStatus(Long productId, Integer status);
}
