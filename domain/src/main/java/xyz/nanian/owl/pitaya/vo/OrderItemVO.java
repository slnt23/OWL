package xyz.nanian.owl.pitaya.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单商品明细VO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@Schema(name = "订单商品明细VO")
public class OrderItemVO {

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "商品名称")
    private String productName;

    @Schema(description = "商品图片")
    private String productImage;

    @Schema(description = "单价")
    private BigDecimal unitPrice;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "小计金额")
    private BigDecimal totalPrice;
}

