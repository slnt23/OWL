package xyz.nanian.owl.pitaya.merchant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.merchant.mapper.MerOrderMapper;
import xyz.nanian.owl.pitaya.merchant.order.OrderApi;
import xyz.nanian.owl.pitaya.merchant.service.MerOrderService;
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
public class MerOrderController implements OrderApi {

    private MerOrderService merOrderService;

    public MerOrderController(MerOrderService merOrderService) {
        this.merOrderService = merOrderService;
    }

    @Override
    public void queryOrders(String sellerId) {

    }

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    @Override
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
