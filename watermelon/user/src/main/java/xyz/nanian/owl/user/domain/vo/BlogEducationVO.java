package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "博客教育经历 VO")
public class BlogEducationVO {

    @Schema(description = "学校名称")
    private String school;

    @Schema(description = "学位/专业")
    private String degree;

    @Schema(description = "时间段")
    private String period;
}