package xyz.nanian.owl.sugarcane.domain.vo;


import lombok.Data;

import java.math.BigDecimal;

/**
 * 多地区比较VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class LocationPriceVO {
    /**
     * 地区名称，如"北京"、"上海"、"广州"等
     */
    private String locationName;

    /**
     * 该地区的价格金额
     */
    private BigDecimal price;

}
