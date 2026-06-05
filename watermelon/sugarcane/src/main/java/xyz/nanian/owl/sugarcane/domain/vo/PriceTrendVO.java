package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.util.List;

/**
 * 时间序列VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceTrendVO extends PriceItemVO {

    /**
     * 地点ID
     */
    private Long locationId;

    /**
     * 地点名称，如"北京"、"上海"等
     */
    private String locationName;

    /**
     * 价格趋势数据点列表，按时间升序排列
     */
    private List<PriceTrendPointVO> trend;
}
