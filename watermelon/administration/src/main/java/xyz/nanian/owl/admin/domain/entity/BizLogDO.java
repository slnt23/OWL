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
 * 业务操作日志表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Getter
@Setter
@ToString
@TableName("biz_log")
@Schema(name= "BizLogDO对象", description = "业务操作日志表")
public class BizLogDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 业务模块，如：订单、用户
     */
    @TableField("module")
    @Schema(name = "业务模块，如：订单、用户")
    private String module;

    /**
     * 业务动作，如：创建订单、用户登录
     */
    @TableField("action")
    @Schema(name = "业务动作，如：创建订单、用户登录")
    private String action;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(name = "用户ID")
    private Long userId;

    /**
     * 类名#方法名
     */
    @TableField("method")
    @Schema(name = "类名#方法名")
    private String method;

    /**
     * 是否成功：1成功 0失败
     */
    @TableField("success")
    @Schema(name = "是否成功：1成功 0失败")
    private Boolean success;

    /**
     * 耗时（毫秒）
     */
    @TableField("cost")
    @Schema(name = "耗时（毫秒）")
    private Long cost;

    /**
     * 错误信息（失败时）
     */
    @TableField("error_msg")
    @Schema(name = "错误信息（失败时）")
    private String errorMsg;

    /**
     * 链路追踪ID
     */
    @TableField("trace_id")
    @Schema(name = "链路追踪ID")
    private String traceId;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;
}
