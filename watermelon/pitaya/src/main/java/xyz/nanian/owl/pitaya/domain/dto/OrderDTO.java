package xyz.nanian.owl.pitaya.domain.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DTO
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Data
@Schema(name = "订单DTO")
public class OrderDTO {

    @Schema(description = "下单来源", example = "CART")
    private String source; // CART / BUY_NOW

    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "收货地址ID")
    private Long addressId;

    @Schema(description = "商品列表")
    private List<OrderItemCreateDTO> items;


    @Data
    @Schema(name = "订单商品DTO")
    public static class OrderItemCreateDTO {

        @Schema(description = "商品ID", example = "1")
        private Long productId;

        @Schema(description = "购买数量", example = "2")
        private Integer quantity;
    }

}
