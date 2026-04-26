package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收货地址VO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@Schema(name = "收货地址VO")
public class AddressVO {

    @Schema(description = "收件人姓名")
    private String receiverName;

    @Schema(description = "收件人手机号")
    private String receiverPhone;

    @Schema(description = "省")
    private String province;

    @Schema(description = "城")
    private String city;

    @Schema(description = "县")
    private String area;

    @Schema(description = "详细地址")
    private String detailAddress;
}

