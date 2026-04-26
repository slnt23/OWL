package xyz.nanian.owl.pitaya.plan;


import xyz.nanian.owl.pitaya.domain.dto.ProductDTO;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 商品管理
 *
 * @author slnt23
 * @since 2026/1/6
 */

public interface MerchantProductApi {

    /**
     * 商品新增
     *
     * @param productDTO DTO
     */
    Result<ResultStatus> addProduct(ProductDTO productDTO);

    /**
     * 修改商品
     * @param productDTO DTO
     */
    Result<ResultStatus> updateProduct(ProductDTO productDTO);

    /**
     * 删除商品
     * @param productId id
     */
    Result<ResultStatus> deleteProduct(Long productId);

    /**
     * 上下架 ，通过商品ID
     * @param productId productID
     * @param productStatus status
     * @return string
     */
    Result<ResultStatus> updateProductStatus(Long productId, Integer productStatus);


//    /**
//     * 上传商品图片,TODO，提到技术OSS或本地上传
//     * @param productName 商品名
//     * @param image 商品图片
//     * @return 新增商品图片数量
//     */
//    Integer addProductImage(String productName, byte[] image);

}
