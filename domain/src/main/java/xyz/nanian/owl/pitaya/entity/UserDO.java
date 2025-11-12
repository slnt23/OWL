package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("user")
public class UserDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_code", exist = true)
    private String userCode;

    @TableField(value = "username", exist = true)
    private String username;

    @TableField(value = "email", exist = true)
    private String email;

    @TableField(value = "password", exist = true)
    private String password;

    @TableField(value = "remark", exist = true)
    private String remark;

    @TableField(value = "enabled", exist = true)
    private Boolean enabled = true;

    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime createTime;

    @TableField(value = "last_login", exist = true)
    private LocalDateTime lastLogin;
}

