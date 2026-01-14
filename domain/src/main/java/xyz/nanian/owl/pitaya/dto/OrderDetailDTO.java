package xyz.nanian.owl.pitaya.dto;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 订单详情表
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "订单详情DTO")
public class OrderDetailDTO {

    @Schema(description = "订单详情编码",example = "UUID")
    String detailCode;

    @Schema(description = "订单编码",example = "UUID")
    String orderCode;

    @Schema(description = "用户名",example = "秦明")
    String userName;

    @Schema(description = "商品数量",example = "23")
    String productNumber;

    @Schema(description = "商品名",example = "火龙果")
    String productName;

    @Schema(description = "商品单价",example = "20")
    String productPrice;

    @Schema(description = "总价格",example = "100")
    String totalPrice;

    @Schema(description = "备注",example = "备注")
    String remark;
}
