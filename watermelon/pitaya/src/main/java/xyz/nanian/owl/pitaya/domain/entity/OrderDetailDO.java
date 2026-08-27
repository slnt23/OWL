package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "订单明细表", description = "订单明细表")
public class OrderDetailDO {

    @Schema(description = "订单明细ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称（下单时的快照）")
    private String productName;

    @Schema(description = "商品图片（下单时的快照）")
    private String productImage;

    @Schema(description = "单价（下单时的价格）")
    private BigDecimal unitPrice;

    @Schema(description = "购买数量")
    private Integer quantity;

    @Schema(description = "小计金额 = 单价 × 数量")
    private BigDecimal totalPrice;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}