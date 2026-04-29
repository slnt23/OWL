package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 密码登陆DTO
 *
 * @author slnt23
 * @since 2026/4/10
 */

@Data
@Schema(name = "密码登陆DTO")
public class PasswordLoginDTO {

    /**
     * 邮件地址
     */
    @Email
    @NotNull
    @Schema(description = "邮件地址",example = "1693676136@qq.com")
    String email;

    /**
     * 密码
     */
    @NotNull
    @Schema(description = "密码",example = "123456")
    String password;

    /**
     * 角色
     */
    @Schema(description = "0=用户，1=商家，10086=管理员",example = "0")
    private Integer role;
}
