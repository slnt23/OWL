package xyz.nanian.owl.caishen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提醒规则出参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "提醒规则出参")
public class FundAlertVO {

    @Schema(description = "提醒规则 ID")
    private Long id;

    @Schema(description = "关注 ID")
    private Long watchId;

    @Schema(description = "基金代码")
    private String fundCode;

    @Schema(description = "基金名称")
    private String fundName;

    @Schema(description = "提醒类型：RISE_ABOVE / FALL_BELOW")
    private String alertType;

    @Schema(description = "绝对净值阈值")
    private BigDecimal thresholdValue;

    @Schema(description = "涨跌幅阈值（%）")
    private BigDecimal thresholdPercent;

    @Schema(description = "状态：ACTIVE / TRIGGERED / PAUSED")
    private String status;

    @Schema(description = "最近触发时间")
    private LocalDateTime lastTriggeredAt;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
