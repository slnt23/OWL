package xyz.nanian.owl.pitaya.product;


import org.springframework.http.ResponseEntity;
import xyz.nanian.owl.pitaya.query.ProductByQuery;
import xyz.nanian.owl.result.Result;

/**
 * 商品模块对外接口
 *
 * @author slnt23
 * @since 2025/11/10
 */

public interface ProductApi {

    /**
     * 通过商品名查询，关键词模糊搜索
     * @param productName 商品名
     * @return 商品信息
     */
    Result<Object> productByName(String productName);

    /**
     * 商品列表分类查询
     * @param sellerId 商家Id
     * @return 分页包装的该商家的所有商品信息
     */
    Result<Object> queryProduct(String sellerId);

    /**
     * 返回商品分类，包括多级分类
     * @return 分页包装的分类结构
     */
    Result<Object> queryCategory();

    /**
     * 通过商品名 更新此商品信息
     * @param productName 商品名
     */
    void updateProductByName(String productName);

    /**
     * 新增商品
     * @param productName 商品名 TODO 后续改改，要添加所有商品信息的，用DTO
     */
    void addProductByName(String productName);

//    /**
//     * 下载商品图片,目前这个没用
//     * @param productName 商品名
//     * @return 图片二进制
//     */
//    ResponseEntity<byte[]> getProductImage(String productName);

    /**
     * 上传商品图片,TODO，提到技术OSS或本地上传
     * @param productName 商品名
     * @param image 商品图片
     */
    void uploadProductImage(String productName, byte[] image);

    /**
     * 导出商品相关信息，以 Excel 格式导出
     * @param productByQuery 商品查询所需参数
     * @return 所返回的 Excel 数据
     */
    ResponseEntity<byte[]> exportProductInfo(ProductByQuery productByQuery);

    /**
     * 通过商品名删除此商品
     * @param productName 商品名
     */
    void deleteProductByName(String productName);

    /**
     * 商品上下架
     */
    void updateProductStatus();

}
