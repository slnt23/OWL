package xyz.nanian.owl.pitaya.merchant.order;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.query.OrderQuery;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 订单管理
 *
 * @author slnt23
 * @since 2026/1/6
 */

public interface OrderApi {

    /**
     * 订单列表，查询用户，TODO 使用分页返回
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    Result<PageResult<OrderListVO>> queryOrders(Integer pageNum,Integer pageSize,Long userId);

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
