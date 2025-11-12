package xyz.nanian.owl.admin;


import xyz.nanian.owl.pitaya.dto.OrderDTO;

/**
 * 订单管理
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface OrderManageApi {


    void countOrders(OrderDTO orderDTO);
}
