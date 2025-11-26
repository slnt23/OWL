package xyz.nanian.owl.pitaya.product;


import org.springframework.http.ResponseEntity;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;
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
     * 通过商品名查询，关键词模糊搜索,商品列表分类查询，
     * @param query 商品名
     * @return 商品信息
     */
    Result<PageResult<ProductVO>> productByName(ProductQuery query);

    /**
     * 商品详情，
     * @param sellerId 商家Id
     * @return 分页包装的该商家的所有商品信息
     */
    Result<PageResult<ProductVO>> queryProduct(String sellerId);

    /**
     * 返回商品分类，包括多级分类，
     * @return 分页包装的分类结构
     */
    Result<PageResult<CategoryVO>> queryCategory();

    /**
     * 通过商品名，更新商品
     * @param productName 商品名
     * @return 更改的结果数量
     */
    Integer updateProductByName(String productName);

    /**
     * 新增商品
     * @param productName 商品名 TODO 后续改改，要添加所有商品信息的，用DTO
     * @return 新增商品数量
     */
    Integer addProductByName(String productName);

    /**
     * 上传商品图片,TODO，提到技术OSS或本地上传
     * @param productName 商品名
     * @param image 商品图片
     * @return 新增商品图片数量
     */
    Integer uploadProductImage(String productName, byte[] image);

    /**
     * 导出商品相关信息，以 Excel 格式导出
     * @param productQuery 商品查询所需参数
     * @return 所返回的 Excel 数据
     */
    ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery);

    /**
     * 通过商品名删除此商品
     * @param productName 商品名
     * @return 返回删除条数
     */
    Integer deleteProductByName(String productName);

    /**
     * 商品上下架
     * @return 返回更改条数
     */
    Integer updateProductStatus();

}
