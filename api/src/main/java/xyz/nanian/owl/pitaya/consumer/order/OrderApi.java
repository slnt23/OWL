package xyz.nanian.owl.pitaya.consumer.order;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.vo.OrderDetailVO;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 订单模块对外接口
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface OrderApi {


    /**
     * 生成订单，从购物车或者立即购买，
     * @param orderDTO 订单DTO数据
     */
    Result<ResultStatus> addOrder(OrderDTO orderDTO);

//    /**
//     * 模拟支付，因为暂时不可实现微信支付，暂时不做,
//     * 这个实现后就可以更新订单支付时间
//     * @param orderDTO 订单DTO
//     */
//    void payOrder(OrderDTO orderDTO);

//    订单状态流转，
    /**
     * 确认收货
     * @param orderId
     * @return
     */
    Result<ResultStatus> confirmOrder(Long orderId);

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    Result<ResultStatus> cancelOrder(Long orderId);

    /**
     * 订单详情查询
     * @param orderId
     * @return
     */
    Result<OrderDetailVO> queryOrderDetail(Long orderId);

//    订单列表，通过用户ID ,查询当前用户所有的订单， TODO 分页查询，
    /**
     * 订单列表查询
     * @param userId
     */
    void queryOrderList(Long userId);


//    错误案例，都是自己瞎想的，很浅，实际使用有点行不通，
//    /**
//     * 订单详情查询，级联查询订单详情
//     * @param orderDTO 订单DTO
//     */
//    void selectOrder(OrderDTO orderDTO);
//
//    /**
//     * 订单信息更改
//     * @param orderDTO 订单DTO数据
//     */
//    Result<ResultStatus> updateOrder(OrderDTO orderDTO);
//
//    /**
//     * 订单明细更改
//     * @param orderDetailDTO 订单明细DTO
//     */
//    Result<ResultStatus> updateOrderDetail(OrderDetailDTO orderDetailDTO);
//
//    /**
//     * 订单删除会级联删除订单明细
//     * @param orderDTO 订单DTO数据
//     */
//    Result<ResultStatus> deleteOrder(OrderDTO orderDTO);
//
//    /**
//     * 订单订单状态
//     * @param orderDTO 订单DTO
//     */
//    void queryOrderStatus(OrderDTO orderDTO);
//
//    /**
//     * 更新订单状态
//     * @param orderDTO 订单DTO
//     */
//    void updateOrderStatus(OrderDTO orderDTO);
//

}
