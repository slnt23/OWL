package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
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
@TableName("orders")
public class OrderDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "order_code", exist = true)
    private String orderCode;

    @TableField(value = "user_id", exist = true)
    private Long userId;

    @TableField(value = "status", exist = true)
    private String status = "待支付";

    @TableField(value = "total_amount", exist = true)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @TableField(value = "order_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime orderTime;

    @TableField(value = "ship_time", exist = true)
    private LocalDateTime shipTime;

    @TableField(value = "cancel_time", exist = true)
    private LocalDateTime cancelTime;

    @TableField(value = "finish_time", exist = true)
    private LocalDateTime finishTime;
}

