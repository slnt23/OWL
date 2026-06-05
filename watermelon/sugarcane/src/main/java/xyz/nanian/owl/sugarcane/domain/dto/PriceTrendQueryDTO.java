package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

@Data
@EqualsAndHashCode(callSuper = true)
public class PriceTrendQueryDTO extends BasePriceQueryDTO {

    @Schema(description = "趋势查询开始时间", example = "2026-01-01T00:00:00")
    @NotNull
    private LocalDateTime startTime;

    @Schema(description = "趋势查询结束时间", example = "2026-06-05T23:59:59")
    @NotNull
    private LocalDateTime endTime;

    @Schema(description = "聚合粒度", example = "DAILY")
    @NotNull
    private TimeGranularity granularity;


    @Override
    public String cacheKey() {
        return super.cacheKey()
                + "|st:" + startTime
                + "|et:" + endTime
                + "|g:" + granularity;
    }
}
