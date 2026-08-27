package xyz.nanian.owl.admin.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 焦点展示 VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "焦点展示VO")
public class SpotlightVO {

    @Schema(description = "焦点ID")
    private Long id;

    @Schema(description = "眉题/前置标题", example = "热门推荐")
    @NotBlank(message = "眉题不能为空")
    private String eyebrow;

    @Schema(description = "主标题", example = "这是一个焦点标题")
    @NotBlank(message = "主标题不能为空")
    private String title;

    @Schema(description = "详细描述", example = "这是焦点的详细描述信息")
    private String description;

    @Schema(description = "配图URL")
    @NotBlank(message = "图片不能为空")
    private String imageUrl;

    @Schema(description = "排序序号，数值越小越靠前", example = "1")
    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;

    @Schema(description = "点击跳转链接")
    private String link;

    @Schema(description = "链接打开方式：_self当前页、_blank新标签页、_parent父框架", example = "_self")
    private String target;

}