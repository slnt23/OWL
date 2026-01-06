package xyz.nanian.owl.pitaya.consumer.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品模块，
 * 其中时间属性在数据库中会自动的生成以及更改，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "商品DTO",description = "用以查询的DTO")
public class ProductDTO {

//    @Schema(description = "商品编号",example = "UUID 生成随机码")
//    String productCode;

    @Schema(description = "商品名",example = "火龙果")
    String productName;

    @Schema(description = "分类名",example = "水果")
    String categoryName;

    @Schema(description = "价格",example = "20元一斤")
    BigDecimal price;

    @Schema(description = "库存",example = "23")
    Integer stock;

    @Schema(description = "封面图URL",example = "www.image.com")
    String coverImage;

//    @Schema(description= "状态",example = "1(1:启用  0：禁用)")
//    Integer status;

//    @Schema(description = "创建时间",example = "2025-11-10 12-10-10")
//    LocalDateTime createTime;

//    @Schema(description= "更新时间",example = "2025-11-10 12-10-10")
//    LocalDateTime updateTime;
}
