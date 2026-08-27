package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 基础信息VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "物品基础信息VO")
public class PriceItemVO {
    @Schema(description = "物品ID")
    private Long itemId;

    @Schema(description = "物品名称")
    private String itemName;

    @Schema(description = "计量单位，如\"个\"、\"件\"、\"千克\"等")
    private String unit;

    @Schema(description = "物品规格描述，如\"256GB\"、\"500ml\"等")
    private String specification;

    @Schema(description = "所属分类名称，如\"电子产品\"、\"食品饮料\"等")
    private String categoryName;
}