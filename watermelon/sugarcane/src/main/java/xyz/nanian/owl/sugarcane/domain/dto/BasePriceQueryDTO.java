package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "价格查询基础DTO")
public abstract class BasePriceQueryDTO {

    // ==================== 物品相关 ====================
    @Schema(description = "单个物品ID", example = "1")
    @Positive(message = "物品ID必须为正整数")
    private Long itemId;

    @Schema(description = "物品编码", example = "SUGAR_WHITE_001")
    @Size(max = 64, message = "物品编码长度不能超过64个字符")
    private String itemCode;

    @Schema(description = "多个物品ID列表", example = "[1, 2, 3]")
    @Size(max = 100, message = "单次查询的物品ID数量不能超过100个")
    private List<Long> itemIds;
    // ==================== 地点相关 ====================
    @Schema(description = "单个地点ID", example = "1")
    @Positive(message = "地点ID必须为正整数")
    private Long locationId;

    @Schema(description = "多个地点ID列表", example = "[1, 2]")
    @Size(max = 50, message = "单次查询的地点ID数量不能超过50个")
    private List<Long> locationIds;
    // ==================== 来源相关 ====================
    @Schema(description = "价格来源ID列表", example = "[1, 2]")
    @Size(max = 20, message = "单次查询的价格来源数量不能超过20个")
    private List<Long> sourceIds;

    @Schema(description = "最小可靠等级 (1-5)", example = "3")
    @Min(value = 1, message = "可靠等级最小值为1")
    @Max(value = 5, message = "可靠等级最大值为5")
    private Integer minReliability;
    // ==================== 通用过滤 ====================
    @Schema(description = "币种", example = "CNY")
    @Pattern(regexp = "^(CNY|USD|EUR|JPY|GBP|HKD)$",
            message = "币种仅支持CNY、USD、EUR、JPY、GBP、HKD")
    @Size(max = 8, message = "币种长度不能超过8个字符")
    private String currency;

    @Schema(description = "最小可信度 (0-100)", example = "80.00")
    @DecimalMin(value = "0.00", inclusive = true, message = "可信度不能小于0")
    @DecimalMax(value = "100.00", inclusive = true, message = "可信度不能大于100")
    private BigDecimal minConfidence;


    /**
     * 生成缓存 key 字符串（供 SpEL #dto.cacheKey() 调用）
     */
    public String cacheKey() {
        StringBuilder sb = new StringBuilder();
        sb.append("i:").append(itemId);
        sb.append("|ic:").append(itemCode);
        sb.append("|ids:").append(sorted(itemIds));
        sb.append("|l:").append(locationId);
        sb.append("|lids:").append(sorted(locationIds));
        sb.append("|sids:").append(sorted(sourceIds));
        sb.append("|r:").append(minReliability);
        sb.append("|c:").append(currency);
        sb.append("|mc:").append(minConfidence);
        return sb.toString();
    }

    private String sorted(List<?> list) {
        if (list == null || list.isEmpty()) return "[]";
        return list.stream().sorted().toList().toString();
    }
}