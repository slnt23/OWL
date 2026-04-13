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
 * 管理员操作日志表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Getter
@Setter
@ToString
@TableName("admin_log")
@Schema(name= "AdminLogDO对象", description = "管理员操作日志表")
public class AdminLogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 管理员ID
     */
    @TableField("admin_id")
    @Schema(name = "管理员ID")
    private Long adminId;

    /**
     * 操作类型
     */
    @TableField("action")
    @Schema(name = "操作类型")
    private String action;

    /**
     * 操作描述
     */
    @TableField("detail")
    @Schema(name = "操作描述")
    private String detail;

    /**
     * 操作IP
     */
    @TableField("ip")
    @Schema(name = "操作IP")
    private String ip;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
