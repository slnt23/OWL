package xyz.nanian.owl.pitaya.merchant.service;


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
}
