package xyz.nanian.owl.sugarcane.domain.dto;


import jakarta.validation.constraints.*;
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

    // ==================== 物品相关 ====================
    /** 单个物品ID */
    @Positive(message = "物品ID必须为正整数")
    private Long itemId;

    /** 物品编码 */
    @Size(max = 64, message = "物品编码长度不能超过64个字符")
    private String itemCode;

    /** 多个物品ID */
    @Size(max = 100, message = "单次查询的物品ID数量不能超过100个")
    private List<Long> itemIds;
    // ==================== 地点相关 ====================
    /** 单个地点ID */
    @Positive(message = "地点ID必须为正整数")
    private Long locationId;

    /** 多个地点ID */
    @Size(max = 50, message = "单次查询的地点ID数量不能超过50个")
    private List<Long> locationIds;
    // ==================== 来源相关 ====================
    /** 价格来源ID列表 */
    @Size(max = 20, message = "单次查询的价格来源数量不能超过20个")
    private List<Long> sourceIds;

    /** 最小可靠等级 */
    @Min(value = 1, message = "可靠等级最小值为1")
    @Max(value = 5, message = "可靠等级最大值为5")
    private Integer minReliability;
    // ==================== 通用过滤 ====================
    /** 币种 */
    @Pattern(regexp = "^(CNY|USD|EUR|JPY|GBP|HKD)$",
            message = "币种仅支持CNY、USD、EUR、JPY、GBP、HKD")
    @Size(max = 8, message = "币种长度不能超过8个字符")
    private String currency;

    /** 最小可信度 */
    @DecimalMin(value = "0.00", inclusive = true, message = "可信度不能小于0")
    @DecimalMax(value = "100.00", inclusive = true, message = "可信度不能大于100")
    private BigDecimal minConfidence;
}
