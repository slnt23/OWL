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
public class PriceTrendVO {

    private PriceItemVO item;
    private String locationName;

    private List<PriceTrendPointVO> trend;
}
