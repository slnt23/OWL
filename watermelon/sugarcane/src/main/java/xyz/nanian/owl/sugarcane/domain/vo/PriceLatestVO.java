package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 最新价格视图对象
 * 用于返回物品在某个地点的最新价格信息，
 * 包含物品、价格、来源等多个维度的汇总数据。
 *
 * @author slnt23
 * @since 2026/4/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "最新价格视图VO")
public class PriceLatestVO extends PriceItemVO{

    @Schema(description = "地点名称")
    private String locationName;

    @Schema(description = "价格金额")
    private BigDecimal price;

    @Schema(description = "币种，如CNY、USD等")
    private String currency;

    @Schema(description = "价格单位，如元/个、美元/件等")
    private String priceUnit;

    @Schema(description = "价格来源名称")
    private String sourceName;

    @Schema(description = "可靠等级，1-5，数值越大越可靠")
    private Integer reliabilityLevel;

    @Schema(description = "生效时间")
    private LocalDateTime effectiveTime;

    @Schema(description = "可信度，0.00-100.00，百分比值")
    private BigDecimal confidence;
}