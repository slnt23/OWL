package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 时间序列VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceTrendPointVO {
    /**
     * 时间点，表示该价格数据的采集或聚合时间
     */
    private LocalDateTime time;

    /**
     * 该时间点对应的价格金额
     */
    private BigDecimal price;
}


