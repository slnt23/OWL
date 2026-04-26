package xyz.nanian.owl.pitaya.merchant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.result.ResultStatus;
import xyz.nanian.owl.pitaya.merchant.service.MerOrderService;
import xyz.nanian.owl.pitaya.domain.vo.OrderListVO;
import xyz.nanian.owl.result.ResultPage;
import xyz.nanian.owl.result.Result;

/**
 * 商家订单Controller
 *
 * @author slnt23
 * @since 2026/1/18
 */

@RestController
@Tag(name ="商家订单管理",description = "商家订单")
@RequestMapping("/pitaya/order/merchant")
public class MerOrderController {

    private MerOrderService merOrderService;

    public MerOrderController(MerOrderService merOrderService) {
        this.merOrderService = merOrderService;
    }

    /**
     * 订单列表 ，查询所选用户的，
     * @param pageNum
     * @param pageSize
     * @param userId
     * @return
     */
    @GetMapping("/orders")
    @Operation(summary = "订单列表",description = "商家搜查指定用户的订单列表")
    public Result<ResultPage<OrderListVO>> queryOrders(Integer pageNum, Integer pageSize, Long userId) {

        return Result.success(merOrderService.listOrders(pageNum,pageSize,userId));
    }

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    @PutMapping()
    @Operation(summary = "更新订单状态",description = "订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=取消")
    public Result<ResultStatus> updateOrderStatus(@RequestParam Long orderId,
                                                  @RequestParam Integer orderStatus) {

        if(merOrderService.updateOrderStatus(orderId,orderStatus)){
            return Result.success(ResultStatus.SUCCESS);
        }else {
            return Result.fail(ResultStatus.FAIL);
        }
    }
}
