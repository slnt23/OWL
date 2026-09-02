package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "博客技能分类 DTO")
public class BlogSkillCategoryDTO {

    @NotBlank(message = "分类名称不能为空")
    @Schema(description = "分类名称")
    private String category;

    @Schema(description = "技能项列表")
    private List<String> items;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}