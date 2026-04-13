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
@TableName("user")
@Schema(name= "UserDO对象", description = "用户表")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 账号编号
     */
    @TableField("user_code")
    @Schema(name = "账号编号")
    private String userCode;

    /**
     * 用户名
     */
    @TableField("username")
    @Schema(name = "用户名")
    private String username;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    @Schema(name = "密码（加密存储）")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone")
    @Schema(name = "手机号")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    @Schema(name = "邮箱")
    private String email;

    /**
     * 头像URL
     */
    @TableField("avatar")
    @Schema(name = "头像URL")
    private String avatar;

    /**
     * 昵称
     */
    @Schema(name = "昵称")
    @TableField("nickname")
    private String nickname;

    /**
     * 角色：0=用户，1=商家，2=管理员
     */
    @TableField("role")
    @Schema(name = "角色：0=用户，1=商家，2=管理员")
    private Byte role;

    /**
     * 状态：0=正常，1=封禁
     */
    @TableField("status")
    @Schema(name = "状态：0=正常，1=封禁")
    private Byte status;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(name = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @TableField("remark")
    @Schema(name = "备注")
    private String remark;
}
