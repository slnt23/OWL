package xyz.nanian.owl.pitaya.domain.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.dto.PageDTO;

/**
 * 订单查询
 *
 * @author slnt23
 * @since 2025/11/11
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "订单查询",description = "通过订单号查询")
public class OrderDTO extends PageDTO {

    @Schema(description = "订单ID")
    private Long id;

    @Schema(description = "订单号",example = "UUID")
    private String orderCode;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "订单状态")
    private Integer orderStatus;

}
