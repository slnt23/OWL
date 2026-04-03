package xyz.nanian.owl.pitaya.merchant.service;


import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;

/**
 * 商家订单Service
 *
 * @author slnt23
 * @since 2026/1/18
 */

public interface MerOrderService {

    /**
     * 更新状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    Boolean updateOrderStatus(Long orderId,Integer orderStatus);

    /**
     * 查询指定用户订单列表
     * @param pageNum
     * @param pageSize
     * @param searchedUserId
     * @return
     */
    PageResult<OrderListVO> listOrders(Integer pageNum,Integer pageSize,Long searchedUserId);
}
