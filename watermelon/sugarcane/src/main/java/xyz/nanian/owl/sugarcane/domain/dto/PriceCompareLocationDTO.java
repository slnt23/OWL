package xyz.nanian.owl.sugarcane.domain.dto;


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

    // 可选：指定时间（否则默认最新）
    @NotNull
    private LocalDateTime targetTime;

}

