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

    private String sourceName;
    private BigDecimal price;
    private Integer reliabilityLevel;
    private BigDecimal confidence;
}
