package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.api.domain.dto.PageDTO;

/**
 * 我的总结分页查询入参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "总结分页查询入参")
public class SummaryQueryDTO extends PageDTO {

    @Schema(description = "周期类型：DAILY / WEEKLY / MONTHLY，可选")
    private String periodType;
}
