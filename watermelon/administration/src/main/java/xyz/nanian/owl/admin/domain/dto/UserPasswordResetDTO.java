package xyz.nanian.owl.admin.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 后台重置用户密码 DTO
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台重置密码DTO")
public class UserPasswordResetDTO {

    @Schema(description = "新密码")
    @NotBlank(message = "新密码不能为空")
    private String newPassword;
}