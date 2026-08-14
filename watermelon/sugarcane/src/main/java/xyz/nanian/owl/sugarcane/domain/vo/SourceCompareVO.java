package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 多来源对比（可信度对比）
 *
 * @author slnt23
 * @since 2026/4/24
 */
@Data
public class SourceCompareVO {

    /**
     * 价格来源名称，如"京东"、"淘宝"、"官方旗舰店"等
     */
    private String sourceName;

    /**
     * 该来源提供的价格金额
     */
    private BigDecimal price;

    /**
     * 来源可靠等级，1-5，数值越大越可靠
     */
    private Integer reliabilityLevel;

    /**
     * 价格可信度，0.00-100.00，百分比值，数值越高表示该价格越可信
     */
    private BigDecimal confidence;
}
