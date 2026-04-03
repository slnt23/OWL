package xyz.nanian.owl.pitaya.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品类别DTO
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Data
@Schema(name = "商品类别")
public class ProductCategoryDTO {

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
