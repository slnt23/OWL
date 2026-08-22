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

    /**
     * 分类 ID
     */
    @Schema(description = "分类 ID")
    private Long id;

    /**
     * 名称
     */
    @Schema(description = "名称")
    private String name;

    /**
     * URL 标识
     */
    @Schema(description = "URL 标识")
    private String slug;

    /**
     * 该分类下已发布文章数
     */
    @Schema(description = "该分类下已发布文章数")
    private Integer postCount;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
