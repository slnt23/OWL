package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

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
public class PriceLatestVO {

    /**
     * 物品基本信息
     */
    private PriceItemVO item;

    /**
     * 地点名称
     */
    private String locationName;

    /**
     * 价格金额
     */
    private BigDecimal price;

    /**
     * 币种，如CNY、USD等
     */
    private String currency;

    /**
     * 价格单位，如元/个、美元/件等
     */
    private String priceUnit;

    /**
     * 价格来源名称
     */
    private String sourceName;

    /**
     * 可靠等级，1-5，数值越大越可靠
     */
    private Integer reliabilityLevel;

    /**
     * 生效时间
     */
    private LocalDateTime effectiveTime;

    /**
     * 可信度，0.00-100.00，百分比值
     */
    private BigDecimal confidence;
}
