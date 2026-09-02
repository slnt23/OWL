package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(name = "博客教育经历 DTO")
public class BlogEducationDTO {

    @NotBlank(message = "学校名称不能为空")
    @Schema(description = "学校名称")
    private String school;

    @Schema(description = "学位/专业")
    private String degree;

    @Schema(description = "时间段")
    private String period;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}