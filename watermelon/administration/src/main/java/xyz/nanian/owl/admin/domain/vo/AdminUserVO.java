package xyz.nanian.owl.admin.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台用户管理 VO
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台用户管理VO")
public class AdminUserVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户编号")
    private String userCode;

    @Schema(description = "用户名")
    private String username;

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态：0=正常，1=封禁")
    private Byte status;

    @Schema(description = "头像URL")
    private String avatarUrl;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}