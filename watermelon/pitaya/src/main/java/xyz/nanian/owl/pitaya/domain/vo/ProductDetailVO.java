package xyz.nanian.owl.pitaya.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品详情展示
 *
 * @author slnt23
 * @since 2026/1/14
 */

@ToString
@Data
@Schema(name = "商品详情",description = "有关商品的详细信息")
public class ProductDetailVO {

    @Schema(description = "商品ID")
    private Long id;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称（通常需要关联查询出来）")
    private String categoryName;

    @Schema(description = "商家ID")
    private Long sellerId;

    @Schema(description = "商品名称")
    private String name;

    @Schema(description = "商品描述")
    private String description;

    @Schema(description = "单价")
    private BigDecimal price;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "封面图URL（主图）")
    private String coverImg;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "最后更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "商品图片列表")
    List<ProductImageVO> images;

    @Data
    @ToString
    @Schema(name = "商品图片VO")
    public static class ProductImageVO{

        @Schema(description = "图片URL")
        private String imageUrl;

        @Schema(description = "图片排序序号")
        private Integer imageSort;
    }

}