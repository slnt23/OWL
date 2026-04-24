package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 分页查询DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class PricePageQueryDTO extends BasePriceQueryDTO {

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Integer pageNo = 1;
    private Integer pageSize = 20;

}

