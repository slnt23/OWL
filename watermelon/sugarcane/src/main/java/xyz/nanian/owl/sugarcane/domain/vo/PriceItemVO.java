package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

/**
 * 基础信息VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceItemVO {
    /**
     * 物品ID
     */
    private Long itemId;

    /**
     * 物品名称
     */
    private String itemName;

    /**
     * 计量单位，如"个"、"件"、"千克"等
     */
    private String unit;

    /**
     * 物品规格描述，如"256GB"、"500ml"等
     */
    private String specification;

    /**
     * 所属分类名称，如"电子产品"、"食品饮料"等
     */
    private String categoryName;
}

