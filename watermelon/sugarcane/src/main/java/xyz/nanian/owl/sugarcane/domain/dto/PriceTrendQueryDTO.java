package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.sugarcane.constant.TimeGranularity;

import java.time.LocalDateTime;

/**
 * 趋势查询DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PriceTrendQueryDTO extends BasePriceQueryDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 聚合粒度
    private TimeGranularity granularity;

}
