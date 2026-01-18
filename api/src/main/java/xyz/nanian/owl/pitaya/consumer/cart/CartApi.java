package xyz.nanian.owl.pitaya.consumer.cart;


import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.query.ShoppingCartQuery;
import xyz.nanian.owl.pitaya.vo.ShoppingCartVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.constant.ResultStatus;

import java.util.List;

/**
 * 购物车模块Api
 *
 * @author slnt23
 * @since 2025/11/18
 */

public interface CartApi {

    /**
     * 新增购物车中商品
     * @param shoppingCartDTO
     */
    Result<ResultStatus> addProduct(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车中商品
     * @param userId
     * @param productId
     */
    Result<ResultStatus> removeProduct(Long userId,Long productId);

    /**
     * 修改购物车中的商品
     * @param shoppingCartDTO
     */
    Result<ResultStatus> updateProduct(ShoppingCartDTO shoppingCartDTO);

    /**
     * 购物车列表展示, TODO 使用分页返回
     * @param pageNum
     * @param pageSize
     * @return
     */
    Result<PageResult<ShoppingCartVO>> queryCartList(Integer pageNum, Integer pageSize);

//    /**
//     * 总金额计算,为什么后端要有这个接口？这不是前端的活？
//     * @param sellerId
//     */
//    Result<String> queryAllAmount(String sellerId);
}
