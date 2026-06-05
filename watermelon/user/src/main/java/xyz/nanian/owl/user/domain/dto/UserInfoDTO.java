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

    @Schema(description= "用户名",example = "秦明")
    String userName;

    @Schema(description = "昵称",example = "小明")
    String nickname;

    @Schema(description= "邮箱",example = "qq.com")
    String email;

    @Schema(description = "手机号" ,example = "110")
    String phone;

    @Schema(description= "备注",example = "备注")
    String remark;

    @Schema(description = "原手机号",example = "119")
    String rawPhone;

}
