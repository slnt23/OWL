package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, message = "邮箱长度不能超过50")
    @Schema(description = "邮件地址",example = "1693676136@qq.com")
    public String email;
}
