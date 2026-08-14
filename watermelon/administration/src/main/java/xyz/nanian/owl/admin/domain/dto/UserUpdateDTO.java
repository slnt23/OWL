package xyz.nanian.owl.admin.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 后台更新用户 DTO，字段为空表示不修改。
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台更新用户DTO")
public class UserUpdateDTO {

    @Size(max = 50, message = "用户名长度不能超过50")
    private String username;

    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过50")
    private String email;

    @Size(max = 20, message = "手机号长度不能超过20")
    private String phone;

    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    private Long roleId;

    @Schema(description = "状态：0=正常，1=封禁")
    private Byte status;

    @Size(max = 255, message = "备注长度不能超过255")
    private String remark;
}
