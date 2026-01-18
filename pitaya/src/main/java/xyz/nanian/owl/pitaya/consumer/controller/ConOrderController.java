package xyz.nanian.owl.pitaya.consumer.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.consumer.order.OrderApi;
import xyz.nanian.owl.pitaya.consumer.service.ConOrderService;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.query.OrderQuery;
import xyz.nanian.owl.pitaya.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 消费者订单Controller
 *
 * @author slnt23
 * @since 2026/1/17
 */

@RestController
@Tag(name = "消费者订单管理",description = "order")
@RequestMapping("/pitaya/order/consumer")
public class ConOrderController implements OrderApi {

    private ConOrderService conOrderService;

    public ConOrderController(ConOrderService conOrderService) {
        this.conOrderService = conOrderService;
    }

    /**
     * 新增订单
     * @param orderDTO 订单DTO数据
     * @return
     */
    @Override
    @Operation(summary = "新增订单", description = "新增订单")
    @PostMapping
    public Result<ResultStatus> addOrder(@RequestBody OrderDTO orderDTO) {

        if(conOrderService.saveOrder(orderDTO)){
            return Result.success(ResultStatus.SUCCESS);
        }else{
            return Result.fail();
        }
    }

    /**
     * 确认订单
     * @param orderId
     * @return
     */
    @Override
    @Operation(summary = "确认订单",description = "用户确认已收到商品，订单状态变更为已完成")
    @PutMapping("/{orderId}/confirmed")
    public Result<ResultStatus> confirmOrder(@PathVariable Long orderId) {

        if(conOrderService.updateOrder(orderId,3)){
            return Result.success(ResultStatus.SUCCESS);
        }else{
            return Result.fail();
        }
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    @Operation(summary = "取消订单",description = "取消")
    @PutMapping("/{orderId}/cancel")
    public Result<ResultStatus> cancelOrder(@PathVariable Long orderId) {

        if(conOrderService.updateOrder(orderId,4)){
            return Result.success(ResultStatus.SUCCESS);
        }else{
            return Result.fail();
        }
    }

    /**
     * 订单详情查询
     * @param orderId
     * @return
     */
    @Override
    @Operation(summary = "订单详情",description = "order detail")
    @GetMapping("/{orderId}")
    public Result<OrderDetailVO> queryOrderDetail(@PathVariable Long orderId) {

        OrderDetailVO od = conOrderService.getOrderDetail(orderId);
        return Result.success(od);
    }

    /**
     * 查询订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @Operation(summary = "订单列表", description = "order list")
    @GetMapping("/orders")
    public Result<PageResult<OrderListVO>> queryOrderList(Integer pageNum,Integer pageSize) {

        return Result.success(conOrderService.listOrders(pageNum,pageSize));
    }
}
