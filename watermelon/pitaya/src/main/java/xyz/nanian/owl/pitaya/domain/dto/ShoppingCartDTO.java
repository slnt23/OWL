package xyz.nanian.owl.pitaya.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 购物车DTO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@Schema(name = "购物车DTO",description = "用于add,remove,update的购物车DTO")
public class ShoppingCartDTO {

//    @Schema(description = "购物车id",example = "1")
//    Long cartId;

    @Schema(description = "用户ID",example = "1")
    Long userId;

    @Schema(description = "商品ID",example = "1")
    Long productId;

    @Schema(description = "数量",example = "10")
    Integer quantity;

    @Schema(description = "是否勾选,0，未勾选，1，勾选",example = "0")
    Integer checked;
}
