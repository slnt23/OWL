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

    /**
     * ID
     */
    @Schema(description = "ID")
    private Long id;

    /**
     * 学校名称
     */
    @Schema(description = "学校名称")
    private String school;

    /**
     * 学位/专业
     */
    @Schema(description = "学位/专业")
    private String degree;

    /**
     * 时间段
     */
    @Schema(description = "时间段")
    private String period;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
