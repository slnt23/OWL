package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 注册用户使用的信息DTO
 * 可以注册用户/商家
 * 这里注册只使用邮箱
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Data
@Schema(name = "邮箱DTO",description = "注册用户所需要的信息")
public class SendCodeDTO {

    @NotNull
    @Email
    @Schema(description = "邮件地址",example = "1693676136@qq.com")
    public String email;

//    用户名可以随机生成，后续用户自己改
//    @NotNull
//    @Schema(description = "用户名",example = "秦明")
//    String userName;
//    @NotNull
//    @Schema(description = "密码",example = "123456")
//    String password;
//    @NotNull
//    @Schema(description = "手机号",example = "110")
//    String phone;
//    @Schema(description = "角色：只有用户与商家,1:消费者用户，2：商家,(管理员只有后台内定)",example = "1")
//    Integer role;

}
