package xyz.nanian.owl.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：user
 */
@Data
@TableName("user") // 指定对应的数据库表名
public class UserDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名（唯一）
     */
    @TableField("username")
    private String username;

    /**
     * 用户账号编号
     */
    @TableField("user_code")
    private String userCode;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 手机号（唯一）
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 昵称
     */
    @TableField("nickname")
    private String nickname;

    /**
     * 角色：0=用户，1=商家，2=管理员
     */
    @TableField("role")
    private Integer role;

    /**
     * 状态：0=正常，1=封禁
     */
    @TableField("status")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
