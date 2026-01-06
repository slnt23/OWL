package xyz.nanian.owl.pitaya.consumer.product;


import org.springframework.http.ResponseEntity;
import xyz.nanian.owl.pitaya.consumer.query.ProductQuery;
import xyz.nanian.owl.pitaya.consumer.vo.CategoryVO;
import xyz.nanian.owl.pitaya.consumer.vo.ProductVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

/**
 * 商品模块对外接口,
 *
 * @author slnt23
 * @since 2025/11/10
 */

public interface ProductApi {


    /**
     * 查询商品，通过商品名，可模糊搜索，分页查询
     * @param query 商品名
     * @return 商品信息
     */
    Result<PageResult<ProductVO>> queryProductByName(ProductQuery query);

    /**
     * 查询商品，目标商家所有商品
     * @param sellerId 商家Id
     * @return 分页包装的该商家的所有商品信息
     */
    Result<PageResult<ProductVO>> queryProduct(String sellerId);

    /**
     * 查询商品分类，类似拼多多首页多级分类，
     * @return 分页包装的分类结构
     */
    Result<PageResult<CategoryVO>> queryCategory();

    /**
     * 更新商品，通过商品名
     * @param productName 商品名
     * @return 更改的结果数量
     */
    Integer modifyProductByName(String productName);

    /**
     * 新增商品 TODO 后续改改，要添加所有商品信息的，用DTO
     * @param productName 商品名
     * @return 新增商品数量
     */
    Integer addProductByName(String productName);

    /**
     * 上传商品图片,TODO，提到技术OSS或本地上传
     * @param productName 商品名
     * @param image 商品图片
     * @return 新增商品图片数量
     */
    Integer addProductImage(String productName, byte[] image);

    /**
     * 导出商品相关信息，以 Excel 格式导出
     * @param productQuery 商品查询所需参数
     * @return 所返回的 Excel 数据
     */
    ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery);

    /**
     * 删除商品，通过商品名，
     * @param productName 商品名
     * @return 返回删除条数
     */
    Integer deleteProductByName(String productName);

    /**
     * 商品上下架，
     * @return 返回更改条数
     */
    Integer modifyProductStatus();

}
