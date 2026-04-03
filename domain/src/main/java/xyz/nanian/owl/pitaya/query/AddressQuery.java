package xyz.nanian.owl.pitaya.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 地址查询Query
 *
 * @author slnt23
 * @since 2026/1/18
 */

@Data
@Schema(name = "地址查询")
public class AddressQuery {

    @Schema(description = "地址ID")
    private Long addressId;

    @Schema(description = "用户ID")
    private Long userId;

}
