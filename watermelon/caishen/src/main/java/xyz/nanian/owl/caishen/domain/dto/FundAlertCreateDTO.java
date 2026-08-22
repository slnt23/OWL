package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 新增提醒规则入参。
 *
 * <p>thresholdValue 与 thresholdPercent 必须恰好提供其一，由 service 校验。</p>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "新增提醒规则入参")
public class FundAlertCreateDTO {

    @NotBlank(message = "提醒类型不能为空")
    @Schema(description = "提醒类型：RISE_ABOVE / FALL_BELOW")
    private String alertType;

    @Schema(description = "绝对净值阈值，与 thresholdPercent 二选一")
    private BigDecimal thresholdValue;

    @Schema(description = "涨跌幅阈值（%），与 thresholdValue 二选一")
    private BigDecimal thresholdPercent;
}
