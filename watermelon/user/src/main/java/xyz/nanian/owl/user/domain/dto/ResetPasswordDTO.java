package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * [UPGRADE] 忘记密码重置 DTO。
 */
@Data
@Schema(name = "重置密码DTO")
public class ResetPasswordDTO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱地址", example = "1693676136@qq.com")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    @Schema(description = "邮箱验证码", example = "123456")
    private String code;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "owl123456")
    private String newPassword;
}
