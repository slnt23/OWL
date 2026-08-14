package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 地区对比DTO,本质是统一时间点，多地区
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PriceCompareLocationDTO extends BasePriceQueryDTO {

    @Schema(description = "对比目标时间（不填默认最新）", example = "2026-06-05T10:30:00")
    @NotNull
    private LocalDateTime targetTime;

    @Override
    public String cacheKey() {
        return super.cacheKey() + "|tt:" + targetTime;
    }
}

