package xyz.nanian.owl.caishen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 我的关注出参（含基金名称、最新净值、提醒规则数）。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "我的关注出参")
public class FundWatchVO {

    @Schema(description = "关注 ID")
    private Long id;

    @Schema(description = "基金代码")
    private String fundCode;

    @Schema(description = "基金名称")
    private String fundName;

    @Schema(description = "最新单位净值")
    private BigDecimal latestNav;

    @Schema(description = "最新净值日期")
    private LocalDate latestNavDate;

    @Schema(description = "日收益率（%）")
    private BigDecimal dailyReturnRate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "关联提醒规则数量")
    private Integer alertCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
