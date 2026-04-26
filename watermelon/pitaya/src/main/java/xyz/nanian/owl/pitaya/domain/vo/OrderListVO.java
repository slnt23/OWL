package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单列表VO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@Schema(name = "订单列表VO")
public class OrderListVO {

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=取消")
    private Integer orderStatus;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "商品数量")
    private Integer itemCount;

    @Schema(description = "下单时间/支付时间")
    private LocalDateTime createTime;
}
