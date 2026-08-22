package xyz.nanian.owl.caishen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 总结出参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "总结出参")
public class SummaryVO {

    @Schema(description = "总结 ID")
    private Long id;

    @Schema(description = "周期类型：DAILY / WEEKLY / MONTHLY")
    private String periodType;

    @Schema(description = "统计开始日期")
    private LocalDate startDate;

    @Schema(description = "统计结束日期")
    private LocalDate endDate;

    @Schema(description = "总结文本")
    private String summaryText;

    @Schema(description = "状态：PENDING / PROCESSING / SUCCESS / FAILED")
    private String status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
