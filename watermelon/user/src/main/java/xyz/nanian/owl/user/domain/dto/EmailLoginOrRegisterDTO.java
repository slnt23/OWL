package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户邮箱登陆DTO
 *
 * @author slnt23
 * @since 2026/4/9
 */

@Data
@Schema(name="验证码登陆DTO")
public class EmailLoginOrRegisterDTO {

    /**
     * 邮件地址
     */
    @Email
    @NotNull
    @Schema(description = "邮件地址",example = "1693676136@qq.com")
    String email;

    /**
     * 邮箱验证码
     */
    @NotNull
    @Pattern(regexp = "\\d{6}$",message = "验证码必须6位数字")
    @Schema(description = "验证码",example = "123456")
    String code;

    /**
     * 角色
     */
    @Schema(description = "角色(0=用户，1=商家，10086=管理员)",example = "0")
    private Integer role;
}
