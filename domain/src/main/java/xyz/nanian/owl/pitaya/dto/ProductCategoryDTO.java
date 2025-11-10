package xyz.nanian.owl.pitaya.dto;


import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 商品类别DTO
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "商品类别")
public class ProductCategoryDTO {

    @Schema(description = "类别编码",example = "UUID 生成")
    String categoryCode;

    @Schema(description = "类别名",example = "水果类")
    String categoryName;

    @Schema(description = "描述",example = "类别描述")
    String description;

    @Schema(description = "是否启用",example = "1:启用  0：未启用")
    Integer enabled;



}
