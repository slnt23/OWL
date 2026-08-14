package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import xyz.nanian.owl.common.utils.regex.RegexPatterns;

/**
 * [UPGRADE] 用户资料更新 DTO，邮箱变更走独立接口。
 */
@Data
@Schema(name = "用户资料更新DTO")
public class UserInfoUpdateDTO {

    @Size(max = 50, message = "用户名不能超过50个字符")
    @Schema(description = "用户名", example = "Qin")
    private String userName;

    @Size(max = 50, message = "昵称不能超过50个字符")
    @Schema(description = "昵称", example = "小明")
    private String nickname;

    @Pattern(regexp = RegexPatterns.PHONE_REGEX, message = "手机号格式不正确")
    @Schema(description = "手机号", example = "13800138000")
    private String phone;

    @Size(max = 255, message = "备注不能超过255个字符")
    @Schema(description = "备注", example = "备注")
    private String remark;
}
