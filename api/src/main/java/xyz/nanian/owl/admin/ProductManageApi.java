package xyz.nanian.owl.admin;


/**
 * 商品管理对外接口 TODO 审核商品是？ 需要新技术实现？没有就推迟
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface ProductManageApi {

    /**
     * 更新商品状态,上下架
     * @param name 商品名，
     */
    void updateProductStatus(String name);

    /**
     * 通过商品名删除此商品
     * @param productName 商品名
     */
    void deleteProductByName(String productName);




}
