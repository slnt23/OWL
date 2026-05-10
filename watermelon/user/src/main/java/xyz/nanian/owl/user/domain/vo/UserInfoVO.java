package xyz.nanian.owl.user.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户个人信息VO
 *
 * @author slnt23
 * @since 2026/5/6
 */

@Data
@Schema(name = "用户信息VO")
public class UserInfoVO {

    @Schema(description= "用户名",example = "秦明")
    String userName;

    @Schema(description = "昵称",example = "小明")
    String nickName;

    @Schema(description= "邮箱",example = "qq.com")
    String email;

    @Schema(description = "手机号" ,example = "110")
    String phone;

    @Schema(description= "备注",example = "备注")
    String remark;

    @Schema(description = "原手机号",example = "119")
    String rawPhone;

    @Schema(description = "角色",example = "user")
    String role;

    @Schema(description = "头像URL")
    String avatarUrl;

}
