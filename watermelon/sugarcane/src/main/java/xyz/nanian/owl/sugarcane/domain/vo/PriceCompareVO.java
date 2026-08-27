package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * 多地区比较VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
@Schema(name = "多地区价格比较VO")
public class PriceCompareVO {

    @Schema(description = "物品基本信息（物品名称、编码等）")
    private PriceItemVO item;

    @Schema(description = "各地区价格列表，每个元素包含地区名称和对应价格")
    private List<LocationPriceVO> prices;
}