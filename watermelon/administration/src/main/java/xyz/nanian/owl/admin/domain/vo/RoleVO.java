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

    private Long id;

    private String roleName;

    private String description;

    @Schema(description = "是否启用：true=启用，false=禁用")
    private Boolean enabled;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
