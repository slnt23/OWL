package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户自定义DTO
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Data
@Schema(name = "用户InfoDTO")
public class UserInfoDTO {

    @Schema(description = "用户ID",example = "1")
    Long userId;

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

//    @Schema(description= "是否启用",example = "1:启用  0：未启用")
//    String enabled;
//
//    @Schema(description= "创建时间",example = "2025-11-10 12-12-20")
//    LocalDateTime createdTime;
//
//    @Schema(description= "最后登陆时间",example = "2025-12-12 12-12-30")
//    LocalDateTime lastLoginTime;
}
