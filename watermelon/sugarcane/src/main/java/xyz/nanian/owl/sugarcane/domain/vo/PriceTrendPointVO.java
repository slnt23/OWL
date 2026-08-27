package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 时间序列VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "价格趋势数据点VO")
public class PriceTrendPointVO {
    @Schema(description = "时间点，表示该价格数据的采集或聚合时间")
    private LocalDateTime time;

    @Schema(description = "该时间点对应的价格金额")
    private BigDecimal price;
}