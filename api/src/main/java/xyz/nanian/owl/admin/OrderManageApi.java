package xyz.nanian.owl.admin;


import xyz.nanian.owl.admin.query.OrderDateQuery;

/**
 * 订单管理
 *
 *
 * @author slnt23
 * @since 2025/11/13
 */

public interface OrderManageApi {

    /**
     * 统计今日订单数，
     * @param orderDateQuery 订单时间区域查询
     */
    Integer countOrders(OrderDateQuery orderDateQuery);
}
