package xyz.nanian.owl.api.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 共享角色实体，登录校验与后台角色管理共用。
 */
@Getter
@Setter
@ToString
@TableName("role")
@Schema(name = "RoleDO对象", description = "角色表")
public class RoleDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("role_name")
    @Schema(name = "角色名称")
    private String roleName;

    @TableField("description")
    @Schema(name = "描述")
    private String description;

    @TableField("enabled")
    @Schema(name = "是否启用")
    private Boolean enabled;

    @TableField("create_time")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    @TableField("update_time")
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
}
