package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 地区对比DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PriceCompareLocationDTO extends BasePriceQueryDTO {

    // 可选：指定时间（否则默认最新）
    private LocalDateTime targetTime;

}

