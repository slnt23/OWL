package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.nanian.owl.api.domain.dto.PageDTO;

import java.time.LocalDate;

/**
 * 净值历史分页查询入参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "净值历史分页查询入参")
public class FundNavPageQueryDTO extends PageDTO {

    @Schema(description = "开始日期，如 2026-08-01，可选")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @Schema(description = "结束日期，如 2026-08-22，可选")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;
}
