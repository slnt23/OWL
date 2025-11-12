package xyz.nanian.owl.user.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 角色DO
 * 目前：0=用户，1=商家，2=管理员 ;后续可以添加其他的角色：VIP等
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("role")
public class RoleDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "role_name", exist = true)
    private String roleName;

    @TableField(value = "description", exist = true)
    private String description;

    @TableField(value = "enabled", exist = true)
    private Boolean enabled = true;

    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime createTime;
}

