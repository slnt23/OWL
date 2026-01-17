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
@TableName("order_mast")
public class OrderDO {

    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号（唯一）
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 支付状态：0=未支付，1=已支付
     */
    private Integer payStatus;

    /**
     * 订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=取消
     */
    private Integer orderStatus;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 发货时间
     */
    private LocalDateTime deliveryTime;

    /**
     * 完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 收货地址快照（JSON格式）
     */
    private String addressSnapshot;

    /**
     * 下单时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
