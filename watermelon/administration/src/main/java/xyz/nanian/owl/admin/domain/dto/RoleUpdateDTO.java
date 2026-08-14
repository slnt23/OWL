package xyz.nanian.owl.admin.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 后台更新角色 DTO，字段为空表示不修改。
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台更新角色DTO")
public class RoleUpdateDTO {

    @Size(max = 100, message = "角色名称长度不能超过100")
    private String roleName;

    @Size(max = 255, message = "角色描述长度不能超过255")
    private String description;

    @Schema(description = "是否启用：true=启用，false=禁用")
    private Boolean enabled;
}
