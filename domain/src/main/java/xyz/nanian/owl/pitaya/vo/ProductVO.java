package xyz.nanian.owl.pitaya.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品InfoVO,对于商家查询，
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Data
@Schema(name = "商品VO",description = "用以返回前端的VO")
public class ProductVO {

//    @Schema(description = "商品编号",example = "UUID 生成随机码")
//    String productCode;

    @Schema(description = "商品名",example = "火龙果")
    String productName;

    @Schema(description = "类别名",example = "水果")
    String categoryName;

    @Schema(description = "单价",example = "20元一斤")
    BigDecimal price;

    @Schema(description = "库存",example = "23")
    Integer stock;

    @Schema(description= "状态",example = "1(1:启用  0：禁用)")
    Integer status;

//    @Schema(description = "封面图 ")
//    String coverImage; TODO 数据库设置的是使用图片的URL，这里暂时不做，

    @Schema(description = "创建时间",example = "2025-11-10 12-10-10")
    LocalDateTime createTime;

    @Schema(description= "更新时间",example = "2025-11-10 12-10-10")
    LocalDateTime updateTime;
}
