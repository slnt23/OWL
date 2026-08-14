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

    private Long id;

    private String userCode;

    private String username;

    private String nickname;

    private String phone;

    private String email;

    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "状态：0=正常，1=封禁")
    private Byte status;

    private String avatarUrl;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
