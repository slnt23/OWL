package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 教育经历 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "教育经历 VO")
public class EducationVO {

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "学校名称")
    private String school;

    @Schema(description = "学位/专业")
    private String degree;

    @Schema(description = "时间段")
    private String period;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}