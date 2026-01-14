package xyz.nanian.owl.pitaya.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 订单查询
 *
 * @author slnt23
 * @since 2025/11/11
 */

@Data
@Schema(name = "通过订单号查询",description = "通过订单号查询")
public class OrderByQuery {

    @Schema(description = "订单号",example = "UUID")
    private String orderCode;

}
