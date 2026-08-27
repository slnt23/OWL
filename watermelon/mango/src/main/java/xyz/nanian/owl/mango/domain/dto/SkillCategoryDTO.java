package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 技能分类创建/更新入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "技能分类创建/更新入参")
public class SkillCategoryDTO {

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称最大 50 字符")
    @Schema(description = "分类名称")
    private String category;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}