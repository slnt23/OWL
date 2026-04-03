package xyz.nanian.owl.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

/**
 * 注册用户使用的信息DTO
 * 可以注册用户/商家
 * TODO 应该验证数据的格式，以及对数据进行校验，才可以使用数据，可以使用正则表达式
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Data
@Schema(name = "用户注册信息",description = "注册用户所需要的信息")
public class UserRegisterDTO {

    @NotNull
    @Schema(description = "用户名",example = "秦明")
    String userName;

    @NotNull
    @Schema(description = "密码",example = "123456")
    String password;

    @NotNull
    @Schema(description = "手机号",example = "110")
    String phone;

    @Email
    @Schema(description = "邮件地址",example = "@qq.com")
    String email;

    @Schema(description = "角色：只有用户与商家,1:消费者用户，2：商家",example = "1")
    Integer role;

}
