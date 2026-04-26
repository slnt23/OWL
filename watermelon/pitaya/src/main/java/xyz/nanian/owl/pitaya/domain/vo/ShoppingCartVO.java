package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 购物车VO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@ToString
public class ShoppingCartVO {

    @Schema(description = "购物车ID",example = "1")
    private Long cartId;

    @Schema(description = "商品ID",example = "1")
    private Long productId;

    @Schema(description = "数量",example = "10")
    private Integer quantity;

    @Schema(description = "是否勾选，0，未勾选，1，勾选",example = "0")
    private Integer checked;

    @Schema(description = "商品名",example = "火龙果")
    private String productName;

    @Schema(description = "商品单价",example = "10.0")
    private BigDecimal price;

    @Schema(description = "封面图",example = "www.baidu.com")
    private String coverImage;

}
