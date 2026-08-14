package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * [UPGRADE] 换绑邮箱 DTO。
 */
@Data
@Schema(name = "换绑邮箱DTO")
public class EmailBindDTO {

    @NotBlank(message = "新邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "新邮箱地址", example = "new@qq.com")
    private String newEmail;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须是6位数字")
    @Schema(description = "发送到新邮箱的验证码", example = "123456")
    private String code;
}
