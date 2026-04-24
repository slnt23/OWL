package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 基础超类
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public abstract class BasePriceQueryDTO {

    // 物品
    private Long itemId;
    private String itemCode;
    private List<Long> itemIds;

    // 地点
    private Long locationId;
    private List<Long> locationIds;

    // 来源筛选
    private List<Long> sourceIds;
    private Integer minReliability;

    // 通用过滤
    private String currency;
    private BigDecimal minConfidence;
}

