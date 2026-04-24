package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 最新价格VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceLatestVO {

    private PriceItemVO item;

    private String locationName;

    private BigDecimal price;
    private String currency;
    private String priceUnit;

    private String sourceName;
    private Integer reliabilityLevel;

    private LocalDateTime effectiveTime;
    private BigDecimal confidence;
}
