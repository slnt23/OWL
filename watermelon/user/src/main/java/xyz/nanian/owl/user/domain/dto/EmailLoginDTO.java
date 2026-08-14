package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * [UPGRADE] 邮箱验证码登录 DTO，注册登录合一，不再接收 role。
 */
@Data
@Schema(name = "邮箱验证码登录DTO")
public class EmailLoginDTO {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50")
    @Schema(description = "邮箱地址", example = "1693676136@qq.com")
    private String email;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    @Schema(description = "邮箱验证码", example = "123456")
    private String code;
}
