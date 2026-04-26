package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 主订单详情VO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@Schema(name = "订单详情VO")
public class OrderDetailVO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单编号")
    private String orderNo;

    @Schema(description = "订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=取消")
    private Integer orderStatus;

    @Schema(description = "支付状态")
    private Integer payStatus;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "下单时间")
    private LocalDateTime createTime;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "收货地址")
    private AddressVO address;

    @Schema(description = "订单商品列表")
    private List<OrderItemVO> items;
}

