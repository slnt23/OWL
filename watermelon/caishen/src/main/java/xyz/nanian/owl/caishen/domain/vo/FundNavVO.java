package xyz.nanian.owl.caishen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 基金净值出参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "基金净值出参")
public class FundNavVO {

    @Schema(description = "净值记录 ID")
    private Long id;

    @Schema(description = "基金代码")
    private String fundCode;

    @Schema(description = "净值日期")
    private LocalDate navDate;

    @Schema(description = "单位净值")
    private BigDecimal unitNav;

    @Schema(description = "累计净值")
    private BigDecimal accumulatedNav;

    @Schema(description = "日收益率（%）")
    private BigDecimal dailyReturnRate;

    @Schema(description = "数据来源：mock/python/manual")
    private String source;
}
