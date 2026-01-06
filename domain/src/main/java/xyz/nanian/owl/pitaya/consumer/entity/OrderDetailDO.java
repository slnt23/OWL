package xyz.nanian.owl.pitaya.consumer.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细DO
 *
 * @author slnt23
 * @since 2025/11/12
 */


@Data
@TableName("order_detail")
public class OrderDetailDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "detail_code", exist = true)
    private String detailCode;

    @TableField(value = "order_id", exist = true)
    private Long orderId;

    @TableField(value = "product_id", exist = true)
    private Long productId;

    @TableField(value = "quantity", exist = true)
    private Integer quantity = 1;

    @TableField(value = "unit_price", exist = true)
    private BigDecimal unitPrice;

    @TableField(value = "total_price", exist = true, insertStrategy = FieldStrategy.NEVER, updateStrategy = FieldStrategy.NEVER)
    private BigDecimal totalPrice;

    @TableField(value = "remark", exist = true)
    private String remark;
}
