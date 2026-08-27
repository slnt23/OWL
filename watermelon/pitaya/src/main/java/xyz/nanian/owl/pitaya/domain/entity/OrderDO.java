package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单DO
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("order_mast")
@Schema(name = "订单表", description = "订单主表")
public class OrderDO {

    @Schema(description = "订单ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "订单编号（唯一）")
    private String orderNo;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(description = "支付状态：0=未支付，1=已支付")
    private Integer payStatus;

    @Schema(description = "订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=取消")
    private Integer orderStatus;

    @Schema(description = "支付时间")
    private LocalDateTime payTime;

    @Schema(description = "发货时间")
    private LocalDateTime deliveryTime;

    @Schema(description = "完成时间")
    private LocalDateTime finishTime;

    @Schema(description = "收货地址快照（JSON格式）")
    private String addressSnapshot;

    @Schema(description = "下单时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}