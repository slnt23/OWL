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
    private Long itemId;
    private String itemName;
    private String unit;
    private String specification;
    private String categoryName;
}

