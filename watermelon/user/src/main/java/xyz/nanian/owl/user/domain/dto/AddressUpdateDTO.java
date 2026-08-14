package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import xyz.nanian.owl.common.utils.regex.RegexPatterns;

/**
 * [UPGRADE] 更新收货地址 DTO。
 */
@Data
@Schema(name = "更新收货地址DTO")
public class AddressUpdateDTO {

    @NotBlank(message = "收件人姓名不能为空")
    @Size(max = 50, message = "收件人姓名不能超过50个字符")
    @Schema(description = "收件人姓名", example = "小明")
    private String receiverName;

    @NotBlank(message = "收件人电话不能为空")
    @Pattern(regexp = RegexPatterns.PHONE_REGEX, message = "收件人电话格式不正确")
    @Schema(description = "收件人电话", example = "13800138000")
    private String receiverPhone;

    @NotBlank(message = "省不能为空")
    @Size(max = 50, message = "省不能超过50个字符")
    @Schema(description = "省", example = "广东省")
    private String province;

    @NotBlank(message = "市不能为空")
    @Size(max = 50, message = "市不能超过50个字符")
    @Schema(description = "市", example = "深圳市")
    private String city;

    @NotBlank(message = "区/县不能为空")
    @Size(max = 50, message = "区/县不能超过50个字符")
    @Schema(description = "区/县", example = "南山区")
    private String district;

    @NotBlank(message = "详细地址不能为空")
    @Size(max = 255, message = "详细地址不能超过255个字符")
    @Schema(description = "详细地址", example = "科技园路1号")
    private String detail;

    @Schema(description = "是否默认地址", example = "false")
    private Boolean isDefault;
}
