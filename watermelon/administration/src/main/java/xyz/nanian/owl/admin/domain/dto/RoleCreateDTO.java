package xyz.nanian.owl.admin.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 后台新增角色 DTO
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台新增角色DTO")
public class RoleCreateDTO {

    @Schema(description = "角色名称", example = "管理员")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 100, message = "角色名称长度不能超过100")
    private String roleName;

    @Schema(description = "角色描述", example = "系统管理员角色")
    @Size(max = 255, message = "角色描述长度不能超过255")
    private String description;

    @Schema(description = "是否启用", example = "true")
    private Boolean enabled;
}