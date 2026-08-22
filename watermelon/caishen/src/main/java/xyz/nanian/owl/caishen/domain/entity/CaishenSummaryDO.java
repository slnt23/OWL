package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户区间总结记录。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("caishen_summary")
public class CaishenSummaryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户 ID */
    @TableField("user_id")
    private Long userId;

    /** 周期类型：DAILY / WEEKLY / MONTHLY */
    @TableField("period_type")
    private String periodType;

    /** 统计开始日期 */
    @TableField("start_date")
    private LocalDate startDate;

    /** 统计结束日期 */
    @TableField("end_date")
    private LocalDate endDate;

    /** 指标快照 JSON */
    @TableField("metric_snapshot")
    private String metricSnapshot;

    /** AI 生成的总结文本 */
    @TableField("summary_text")
    private String summaryText;

    /** 状态：PENDING / PROCESSING / SUCCESS / FAILED */
    private String status;

    /** 失败时的错误信息 */
    @TableField("error_message")
    private String errorMessage;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;
}
