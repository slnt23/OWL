package xyz.nanian.owl.admin.domain.entity;

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
 * <p>
 * 角色表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Getter
@Setter
@ToString
@TableName("role")
@Schema(name= "RoleDO对象", description = "角色表")
public class RoleDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @Schema(name = "角色名称")
    private String roleName;

    /**
     * 描述
     */
    @Schema(name = "描述")
    @TableField("description")
    private String description;

    /**
     * 是否启用
     */
    @TableField("enabled")
    @Schema(name = "是否启用")
    private Boolean enabled;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
