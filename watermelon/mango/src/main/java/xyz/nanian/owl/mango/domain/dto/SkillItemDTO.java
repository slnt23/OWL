package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 技能条目创建/更新入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "技能条目创建/更新入参")
public class SkillItemDTO {

    /**
     * 所属技能分类 ID
     */
    @NotNull(message = "所属分类 ID 不能为空")
    @Schema(description = "所属分类 ID")
    private Long categoryId;

    /**
     * 技能名称
     */
    @NotBlank(message = "技能名称不能为空")
    @Size(max = 50, message = "技能名称最大 50 字符")
    @Schema(description = "技能名称")
    private String name;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
