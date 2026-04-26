package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

/**
 * 商品分类VO
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Data
@Schema(name = "商品类别")
@ToString
public class CategoryVO {

    @Schema(description = "父分类ID，顶级分类为0", example = "0", defaultValue = "0")
    private Long parentId;

    @Schema(description = "分类名称", example = "水果类")
    private String name;

    @Schema(description = "分类层级（1:一级，2:二级...）", example = "1")
    private Integer level;

    @Schema(description = "排序字段，值越小越靠前", example = "10")
    private Integer sort;

    @Schema(description = "分类描述", example = "各类新鲜水果")
    private String description;

}

