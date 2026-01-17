package xyz.nanian.owl.pitaya.merchant.order;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.result.Result;

/**
 * 订单管理
 *
 * @author slnt23
 * @since 2026/1/6
 */

public interface OrderApi {

    /**
     * 订单列表，查询用户，TODO 使用分页返回
     * @param sellerId
     */
    void queryOrders(String sellerId);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    Result<ResultStatus> updateOrderStatus(Long orderId,Integer orderStatus);

//    /**
//     * 发货
//     * @param orderId
//     * @return
//     */
//    Result<ResultStatus> shipOrder(Long orderId);
}
