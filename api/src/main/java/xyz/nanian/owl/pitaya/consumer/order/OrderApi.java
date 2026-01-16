package xyz.nanian.owl.pitaya.consumer.order;


import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.dto.OrderDetailDTO;
import xyz.nanian.owl.result.Result;

/**
 * 订单模块对外接口 TODO 返回类型尚未确定
 *
 * @author slnt23
 * @since 2025/11/11
 */

public interface OrderApi {


    /**
     * 生成订单，从购物车或者立即购买，
     * TODO 后续实现类的时候根据Service 更改返回数据类型，以及形参
     * @param orderDTO 订单DTO数据
     */
    Result<ResultStatus> autoAddOrder(OrderDTO orderDTO);

//    /**
//     * 模拟支付，因为暂时不可实现微信支付，暂时不做
//     * @param orderDTO 订单DTO
//     */
//    void payOrder(OrderDTO orderDTO);

//    订单状态流转，
//    发货
    Result<ResultStatus> shipOrder();

//    确认收货
    Result<ResultStatus> confirmOrder();

//    取消订单
    Result<ResultStatus> cancelOrder();

//    订单详情查询
    void queryOrderDetail();

//    订单列表，TODO 分页查询，
    void queryOrderList();



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
