package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 分类 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "分类 VO")
public class CategoryVO {

    @Schema(description = "分类 ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "URL 标识")
    private String slug;

    @Schema(description = "该分类下已发布文章数")
    private Integer postCount;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}