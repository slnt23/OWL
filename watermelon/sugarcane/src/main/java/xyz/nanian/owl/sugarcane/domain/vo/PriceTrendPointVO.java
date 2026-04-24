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
    private LocalDateTime time;
    private BigDecimal price;
}


