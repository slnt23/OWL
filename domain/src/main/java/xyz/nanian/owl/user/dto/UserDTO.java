package xyz.nanian.owl.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 用户DTO模块
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "用户DTO")
public class UserDTO {

    @Schema(description= "账号编号",example = "UUid自动生成")
    String userCode;

    @Schema(description= "用户名",example = "秦明")
    String userName;

    @Schema(description= "电子邮件",example = "qq.com")
    String email;

    @Schema(description= "备注",example = "备注")
    String remark;

    @Schema(description= "是否启用",example = "1:启用  0：未启用")
    String enabled;

    @Schema(description= "创建时间",example = "2025-11-10 12-12-20")
    LocalDateTime createdTime;

    @Schema(description= "最后登陆时间",example = "2025-12-12 12-12-30")
    LocalDateTime lastLoginTime;
}
