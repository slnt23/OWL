package xyz.nanian.owl.pitaya.consumer.product;


import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 消费者商品模块
 *
 * @author slnt23
 * @since 2025/11/10
 */

public interface ProductApi {


    /**
     * 查询商品列表，通过商品名，可模糊搜索，分页查询，
     * @param query 商品名
     * @return 商品信息
     */
    Result<PageResult<ProductVO>> queryProductByName(ProductQuery query);

    /**
     * 查询商品分类，类似拼多多首页多级分类，
     * @return 分页包装的分类结构
     */
    Result<List<CategoryVO>> queryCategory();

    /**
     * 商品详情，
     * @return 商品信息
     */
    Result<ProductDetailVO> queryProductDetail(Integer productId);

    /**
     * 更新商品，通过商品名
     * @param productName 商品名
     * @return 更改的结果数量
     */
    Result<Integer> modifyProductByName(String productName);

    /**
     * 新增商品
     * @param productName 商品名
     * @return 新增商品数量
     */
    Integer addProductByName(String productName);

    /**
     * 删除商品，通过商品名，
     * @param productName 商品名
     * @return 返回删除条数
     */
    Integer deleteProductByName(String productName);


    /**
     * 上传商品图片,TODO，提到技术OSS或本地上传
     * @param productName 商品名
     * @param image 商品图片
     * @return 新增商品图片数量
     */
    Integer addProductImage(String productName, byte[] image);

//    /**
//     * 导出商品相关信息，以 Excel 格式导出
//     * @param productQuery 商品查询所需参数
//     * @return 所返回的 Excel 数据
//     */
//    ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery);

//    /**
//     * 商品上下架，
//     * @return 返回更改条数
//     */
//    Integer modifyProductStatus();

//    /**
//     * 查询商品，目标商家所有商品
//     * @param sellerId 商家Id
//     * @return 分页包装的该商家的所有商品信息
//     */
//    Result<PageResult<ProductVO>> queryProduct(String sellerId);

}
