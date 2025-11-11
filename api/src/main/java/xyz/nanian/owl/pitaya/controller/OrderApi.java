package xyz.nanian.owl.pitaya.controller;


import xyz.nanian.owl.pitaya.dto.OrderDTO;

/**
 * 订单模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface OrderApi {


    /**
     * 购买商品后自动添加订单，
     * @param orderDTO 订单DTO数据
     */
    void autoAddOrder(OrderDTO orderDTO);

    /**
     * 订单信息更改
     * @param orderDTO 订单DTO数据
     */
    void updateOrder(OrderDTO orderDTO);

    /**
     * 订单删除
     * @param orderDTO 订单DTO数据
     */
    void deleteOrder(OrderDTO orderDTO);


}
