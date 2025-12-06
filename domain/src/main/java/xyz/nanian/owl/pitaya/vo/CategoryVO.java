package xyz.nanian.owl.pitaya.vo;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品分类VO
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Schema(name = "商品类别")
public class CategoryVO {

    @Schema(description = "父分类ID",example = "0")
    Integer parentId;

    @Schema(description = "分类名称",example = "水果类")
    String categoryName;

    @Schema(description = "分类层级",example = "第一层")
    private Integer level;

    @Schema(description = "排序字段",example = "无")
    private Integer sort;

    @Schema(description = "描述",example = "类别描述")
    String description;


}

