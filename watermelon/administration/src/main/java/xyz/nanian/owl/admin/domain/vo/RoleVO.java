package xyz.nanian.owl.admin.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台角色管理 VO
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Data
@Schema(name = "后台角色管理VO")
public class RoleVO {

    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "是否启用：true=启用，false=禁用")
    private Boolean enabled;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}