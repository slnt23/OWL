package xyz.nanian.owl.admin.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 后台新增用户 DTO
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台新增用户DTO")
public class UserCreateDTO {

    @Schema(description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50")
    private String email;

    @Schema(description = "手机号")
    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "昵称")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100")
    private String roleName;

    @Schema(description = "状态：0=正常，1=封禁")
    private Byte status;

    @Schema(description = "备注")
    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;
}