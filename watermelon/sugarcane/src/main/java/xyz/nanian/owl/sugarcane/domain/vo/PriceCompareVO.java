package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 多地区比较VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceCompareVO {

    private PriceItemVO item;

    private List<LocationPriceVO> prices;
}


