package xyz.nanian.owl.admin.controller;


/**
 * 商品管理 TODO 审核商品是？ 需要新技术实现？没有就推迟
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface ProductManageApi {

    /**
     * 上下架，下架违规商品
     * @param name 商品名，
     */
    void updateProductStatus(String name);

    /**
     * 删除商品，通过商品名
     * @param productName 商品名
     */
    void deleteProductByName(String productName);




}
