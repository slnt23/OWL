package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 写入DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceRecordCreateDTO {

    private Long itemId;
    private Long locationId;

    private BigDecimal price;
    private String currency;
    private String priceUnit;

    private Long sourceId;

    private LocalDateTime effectiveTime;
    private LocalDateTime expireTime;

    private BigDecimal confidence;
}
