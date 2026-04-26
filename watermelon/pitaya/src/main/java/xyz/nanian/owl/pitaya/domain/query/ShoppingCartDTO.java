package xyz.nanian.owl.pitaya.domain.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.dto.PageDTO;

/**
 * 购物车查询
 *
 * @author slnt23
 * @since 2026/1/18
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "购物车查询",description = "购物车相关查询，后面可添加数据")
public class ShoppingCartDTO extends PageDTO {

    /**
     * 这里后期这个用户id应该是通过 jwt 或者cookie查询，
     * 不是通过前端传递
     */
    @Schema(description = "用户id")
    private Long userId;
}
