package xyz.nanian.owl.pitaya.controller;


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
     * 通过商品名查询，
     * @param productName 商品名
     * @return 商品信息
     */
    Result<Object> productByName(String productName);

    /**
     * 通过商品名删除此商品
     * @param productName 商品名
     */
    void deleteProductByName(String productName);

    /**
     * 通过商品名更新此商品
     * @param productName 商品名
     */
    void updateProductByName(String productName);

    /**
     * 新增商品
     * @param productName 商品名 TODO 后续改改，要添加所有商品信息的，
     */
    void addProductByName(String productName);

    /**
     * 下载商品图片
     * @param productName 商品名
     * @return 图片二进制
     */
    ResponseEntity<byte[]> getProductImage(String productName);

    /**
     * 上传商品图片,
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
}
