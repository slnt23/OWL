//package xyz.nanian.owl.pitaya.plan;
//
//
//import xyz.nanian.owl.result.ResultStatus;
//import xyz.nanian.owl.pitaya.vo.OrderListVO;
//import xyz.nanian.owl.result.ResultPage;
//import xyz.nanian.owl.result.Result;
//
///**
// * 订单管理
// *
// * @author slnt23
// * @since 2026/1/6
// */
//
//public interface MerchantOrderApi {
//
//    /**
//     * 订单列表，查询用户，TODO 使用分页返回
//     * @param pageNum
//     * @param pageSize
//     * @param userId
//     * @return
//     */
//    Result<ResultPage<OrderListVO>> queryOrders(Integer pageNum, Integer pageSize, Long userId);
//
//    /**
//     * 修改订单状态
//     * @param orderId
//     * @param orderStatus
//     */
//    Result<ResultStatus> updateOrderStatus(Long orderId,Integer orderStatus);
//
//    /**
//     * 发货
//     * @param orderId
//     * @return
//     */
//    Result<ResultStatus> shipOrder(Long orderId);
//}
