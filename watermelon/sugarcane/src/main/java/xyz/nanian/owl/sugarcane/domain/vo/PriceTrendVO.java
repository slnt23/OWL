package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 时间序列VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "价格趋势VO")
public class PriceTrendVO extends PriceItemVO {

    @Schema(description = "地点ID")
    private Long locationId;

    @Schema(description = "地点名称，如\"北京\"、\"上海\"等")
    private String locationName;

    @Schema(description = "价格趋势数据点列表，按时间升序排列")
    private List<PriceTrendPointVO> trend;
}