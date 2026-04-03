package xyz.nanian.owl.pitaya.consumer.service;


import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.query.OrderQuery;
import xyz.nanian.owl.pitaya.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;

import java.util.List;

/**
 * 消费者订单Service
 *
 * @author slnt23
 * @since 2026/1/17
 */

public interface ConOrderService {

    /**
     * 新增订单
     * @param orderDTO
     * @return
     */
    Boolean saveOrder(OrderDTO orderDTO);

    /**
     * update order
     * @param orderId
     * @param orderStatus
     * @return
     */
    Boolean updateOrder(Long orderId,Integer orderStatus);

    /**
     * select order detail
     * @param orderId
     * @return
     */
    OrderDetailVO getOrderDetail(Long orderId);

    /**
     * order list
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<OrderListVO> listOrders(Integer pageNum,Integer pageSize);
}
