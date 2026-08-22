package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 手动触发总结生成入参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "手动触发总结生成入参")
public class SummaryTriggerDTO {

    @NotBlank(message = "周期类型不能为空")
    @Schema(description = "周期类型：DAILY / WEEKLY / MONTHLY")
    private String periodType;
}
