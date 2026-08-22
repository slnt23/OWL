package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 基金提醒规则。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("caishen_fund_alert")
public class CaishenFundAlertDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 关联 caishen_fund_watch.id，级联删除 */
    @TableField("watch_id")
    private Long watchId;

    /** 提醒类型：RISE_ABOVE / FALL_BELOW */
    @TableField("alert_type")
    private String alertType;

    /** 绝对净值阈值 */
    @TableField("threshold_value")
    private BigDecimal thresholdValue;

    /** 涨跌幅阈值（%） */
    @TableField("threshold_percent")
    private BigDecimal thresholdPercent;

    /** 状态：ACTIVE / TRIGGERED / PAUSED */
    private String status;

    /** 最近一次触发时间 */
    @TableField("last_triggered_at")
    private LocalDateTime lastTriggeredAt;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
