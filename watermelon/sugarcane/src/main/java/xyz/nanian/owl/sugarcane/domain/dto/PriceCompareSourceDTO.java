package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 来源对比DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "来源价格对比DTO")
public class PriceCompareSourceDTO extends BasePriceQueryDTO {

    @Schema(description = "对比目标时间", example = "2026-06-05T10:30:00")
    @NotNull
    private LocalDateTime targetTime;

    @Override
    public String cacheKey() {
        return super.cacheKey() + "|tt:" + targetTime;
    }
}