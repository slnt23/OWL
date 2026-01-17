package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单明细DO
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("order_detail")
public class OrderDetailDO {

    /**
     * 订单明细ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称（下单时的快照）
     */
    private String productName;

    /**
     * 商品图片（下单时的快照）
     */
    private String productImage;

    /**
     * 单价（下单时的价格）
     */
    private BigDecimal unitPrice;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 小计金额 = 单价 × 数量
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
