package xyz.nanian.owl.pitaya.order;


import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.dto.OrderDetailDTO;

/**
 * 订单模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface OrderApi {


    /**
     * 购买商品后自动生成订单，
     * TODO 后续实现类的时候根据Service 更改返回数据类型，以及形参
     * @param orderDTO 订单DTO数据
     */
    void autoAddOrder(OrderDTO orderDTO);

    /**
     * 订单详情查询，级联查询订单详情
     * @param orderDTO 订单DTO
     */
    void selectOrder(OrderDTO orderDTO);

    /**
     * 订单信息更改
     * @param orderDTO 订单DTO数据
     */
    void updateOrder(OrderDTO orderDTO);

    /**
     * 订单删除会级联删除订单明细
     * @param orderDTO 订单DTO数据
     */
    void deleteOrder(OrderDTO orderDTO);

    /**
     * 订单明细更改
     * @param orderDetailDTO 订单明细DTO
     */
    void updateOrderDetail(OrderDetailDTO orderDetailDTO);

    /**
     * 查看订单状态
     * @param orderDTO 订单DTO
     */
    void queryOrderStatus(OrderDTO orderDTO);

    /**
     * 更新订单状态
     * @param orderDTO 订单DTO
     */
    void updateOrderStatus(OrderDTO orderDTO);

}
