package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 写入DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceRecordCreateDTO {

    @Schema(description = "物品ID", example = "1")
    private Long itemId;

    @Schema(description = "地点ID", example = "1")
    private Long locationId;

    @Schema(description = "价格", example = "99.50")
    private BigDecimal price;

    @Schema(description = "币种", example = "CNY")
    private String currency;

    @Schema(description = "价格单位", example = "元/斤")
    private String priceUnit;

    @Schema(description = "来源ID", example = "1")
    private Long sourceId;

    @Schema(description = "生效时间", example = "2026-06-05T10:30:00")
    private LocalDateTime effectiveTime;

    @Schema(description = "过期时间", example = "2026-12-31T23:59:59")
    private LocalDateTime expireTime;

    @Schema(description = "可信度 (0-100)", example = "95.00")
    private BigDecimal confidence;
}
