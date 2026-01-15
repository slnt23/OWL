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
     * 商品列表，通过商品名查询，可模糊搜索，分页查询，
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
     * 商品搜索, 搜索指定商品，但是名字相似注定返回多个，所以list<> 但已实现，可搁置
     * @param productName 商品名
     * @return 商品VO
     */
    Result<ProductVO>  queryProductByName(String productName);
//    /**
//     * 更新商品，通过商品名
//     * @param productId 商品id
//     * @return 更改的结果数量
//     */
//    Result<String> modifyProductByName(Integer productId);
//
//    /**
//     * 新增商品
//     * @param productName 商品名
//     * @return 新增商品数量
//     */
//    Integer addProductByName(String productName);
//
//    /**
//     * 删除商品，通过商品名，
//     * @param productName 商品名
//     * @return 返回删除条数
//     */
//    Integer deleteProductByName(String productName);
//
//
//    /**
//     * 导出商品相关信息，以 Excel 格式导出
//     * @param productQuery 商品查询所需参数
//     * @return 所返回的 Excel 数据
//     */
//    ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery);
//
//    /**
//     * 商品上下架，
//     * @return 返回更改条数
//     */
//    Integer modifyProductStatus();
//
//    /**
//     * 查询商品，目标商家所有商品
//     * @param sellerId 商家Id
//     * @return 分页包装的该商家的所有商品信息
//     */
//    Result<PageResult<ProductVO>> queryProduct(String sellerId);

}
