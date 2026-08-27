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
 * 用户表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Getter
@Setter
@ToString
@TableName("user_account")
@Schema(name = "用户表", description = "用户表")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "账号编号")
    @TableField("user_code")
    private String userCode;

    @Schema(description = "用户名")
    @TableField("username")
    private String username;

    @Schema(description = "密码（加密存储）")
    @TableField("password")
    private String password;

    @Schema(description = "手机号")
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

    @Schema(description = "角色名称")
    @TableField("role_name")
    private String roleName;

    @Schema(description = "状态：0=正常，1=封禁")
    @TableField("status")
    private Byte status;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    @TableField("remark")
    private String remark;
}