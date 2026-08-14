package xyz.nanian.owl.user.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户个人信息VO
 *
 * @author slnt23
 * @since 2026/5/6
 */

@Data
@Schema(name = "用户信息VO")
public class UserInfoVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "账号编号")
    private String userCode;

    @Schema(description= "用户名",example = "秦明")
    private String userName;

    @Schema(description = "昵称",example = "小明")
    private String nickname;

    /**
     * [TO_BE_DELETED] 旧字段，使用 nickname。
     */
    @Deprecated
    @Schema(description = "昵称-旧字段", example = "小明")
    private String nickName;

    @Schema(description= "邮箱",example = "qq.com")
    private String email;

    @Schema(description = "手机号" ,example = "110")
    private String phone;

    @Schema(description= "备注",example = "备注")
    private String remark;

    /**
     * [TO_BE_DELETED] 冗余字段。
     */
    @Deprecated
    @Schema(description = "原手机号-冗余字段", example = "119")
    private String rawPhone;

    @Schema(description = "角色",example = "user")
    private String role;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
