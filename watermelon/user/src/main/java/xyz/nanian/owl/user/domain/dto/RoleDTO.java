package xyz.nanian.owl.user.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 角色DTO,对于新版架构可能没有太大的用处，但是可以作为扩展，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name= "角色DTO")
public class RoleDTO {

    @Schema(description= "角色名称",example = "普通用户")
    String roleName;

    @Schema(description= "角色描述",example = "只拥有基础权限")
    String roleDesc;

    @Schema(description= "是否启用",example = "1:启用  0：未启用")
    Integer enabled;

    @Schema(description= "创建时间",example = "2025-11-10 12-12-12")
    LocalDateTime createTime;
}
