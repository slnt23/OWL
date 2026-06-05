package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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

    @Schema(description = "查询开始时间", example = "2026-01-01T00:00:00")
    @NotNull
    private LocalDateTime startTime;

    @Schema(description = "查询结束时间", example = "2026-06-05T23:59:59")
    @NotNull
    private LocalDateTime endTime;

    @Schema(description = "页码", example = "1")
    private Integer pageNo = 1;

    @Schema(description = "每页大小", example = "20")
    private Integer pageSize = 20;

}

