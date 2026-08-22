package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 分类创建/更新入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "分类创建/更新入参")
public class CategoryDTO {

    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称最大 50 字符")
    @Schema(description = "分类名称")
    private String name;

    /**
     * URL 标识
     */
    @Schema(description = "URL 标识")
    private String slug;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
