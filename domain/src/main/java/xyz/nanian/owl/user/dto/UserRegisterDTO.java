package xyz.nanian.owl.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 注册用户使用的信息DTO
 * 可以注册用户/商家
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Schema(name = "用户注册信息",description = "注册用户所需要的信息")
public class UserRegisterDTO {

    @Schema(description = "用户名",example = "秦明")
    String userName;

    @Schema(description = "密码",example = "123456")
    String password;

    @Schema(description = "手机号",example = "110")
    String phone;

    @Schema(description = "邮件地址",example = "@qq.com")
    String email;

    @Schema(description = "角色：只有用户与商家",example = "1:用户，2：商家")
    Integer role;

}
