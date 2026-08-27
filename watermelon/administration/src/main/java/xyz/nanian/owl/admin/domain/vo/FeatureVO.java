package xyz.nanian.owl.admin.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 产品特性 VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "产品特性VO")
public class FeatureVO {

    @Schema(description = "特性ID")
    private Long id;

    @Schema(description = "图标标识", example = "star")
    @NotBlank(message = "图标不能为空")
    private String icon;

    @Schema(description = "特性标题", example = "高性能")
    @NotBlank(message = "标题不能为空")
    private String title;

    @Schema(description = "特性描述", example = "采用先进架构，毫秒级响应")
    private String description;

    @Schema(description = "排序序号，数值越小越靠前", example = "1")
    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;
}