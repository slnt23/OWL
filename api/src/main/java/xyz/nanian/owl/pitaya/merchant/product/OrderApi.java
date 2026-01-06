package xyz.nanian.owl.pitaya.merchant.product;


import xyz.nanian.owl.pitaya.consumer.dto.OrderDTO;

/**
 * 订单管理
 *
 * @author slnt23
 * @since 2026/1/6
 */

public interface OrderApi {

    /**
     * 查看用户订单列表
     * @param sellerId
     */
    void queryOrders(String sellerId);

    /**
     * 修改订单状态
     * @param orderDTO
     */
    void updateOrderStatus(OrderDTO orderDTO);
}
