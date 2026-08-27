package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 教育经历创建/更新入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "教育经历创建/更新入参")
public class EducationDTO {

    @NotBlank(message = "学校名称不能为空")
    @Schema(description = "学校名称")
    private String school;

    @NotBlank(message = "学位/专业不能为空")
    @Schema(description = "学位/专业")
    private String degree;

    @NotBlank(message = "时间段不能为空")
    @Schema(description = "时间段")
    private String period;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}