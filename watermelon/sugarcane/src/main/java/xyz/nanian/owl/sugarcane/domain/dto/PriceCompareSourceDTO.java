package xyz.nanian.owl.sugarcane.domain.dto;


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
public class PriceCompareSourceDTO extends BasePriceQueryDTO {

    private LocalDateTime targetTime;

}
