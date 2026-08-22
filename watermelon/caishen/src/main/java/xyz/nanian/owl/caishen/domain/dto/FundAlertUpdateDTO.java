package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 更新提醒规则入参（部分更新，字段非空即更新）。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "更新提醒规则入参")
public class FundAlertUpdateDTO {

    @Schema(description = "提醒类型：RISE_ABOVE / FALL_BELOW")
    private String alertType;

    @Schema(description = "绝对净值阈值")
    private BigDecimal thresholdValue;

    @Schema(description = "涨跌幅阈值（%）")
    private BigDecimal thresholdPercent;

    @Schema(description = "状态：ACTIVE / PAUSED")
    private String status;
}
