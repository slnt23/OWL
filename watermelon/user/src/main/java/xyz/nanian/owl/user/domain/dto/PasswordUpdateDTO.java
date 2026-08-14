package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * [UPGRADE] 登录后修改密码 DTO。
 */
@Data
@Schema(name = "修改密码DTO")
public class PasswordUpdateDTO {

    @Schema(description = "旧密码；账号未设置密码时可省略", example = "old123456")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", example = "owl123456")
    private String newPassword;
}
