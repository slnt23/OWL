package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 标签 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "标签 VO")
public class TagVO {

    @Schema(description = "标签 ID")
    private Long id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "URL 标识")
    private String slug;

    @Schema(description = "该标签下已发布文章数")
    private Integer postCount;
}