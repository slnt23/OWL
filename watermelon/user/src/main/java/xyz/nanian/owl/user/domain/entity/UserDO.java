package xyz.nanian.owl.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类
 * 对应数据库表：user_account
 */
@Data
@TableName("user_account")
@Schema(name = "用户表", description = "用户表")
public class UserDO {

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户名（唯一）")
    @TableField("username")
    private String userName;

    @Schema(description = "用户账号编号（唯一）")
    @TableField("user_code")
    private String userCode;

    @Schema(description = "密码（加密存储）")
    @TableField("password")
    private String password;

    @Schema(description = "手机号（唯一）")
    @TableField("phone")
    private String phone;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "头像URL")
    @TableField("avatar_url")
    private String avatarUrl;

    @Schema(description = "昵称")
    @TableField("nickname")
    private String nickname;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;

    @Schema(description = "角色名称")
    @TableField("role_name")
    private String roleName;

    @Schema(description = "状态：0=正常，1=封禁")
    @TableField("status")
    private Integer status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}