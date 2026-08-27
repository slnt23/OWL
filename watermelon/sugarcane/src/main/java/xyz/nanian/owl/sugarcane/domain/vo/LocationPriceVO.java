package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 多地区比较VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "地区价格VO")
public class LocationPriceVO {
    @Schema(description = "地区名称，如\"北京\"、\"上海\"、\"广州\"等")
    private String locationName;

    @Schema(description = "该地区的价格金额")
    private BigDecimal price;

}