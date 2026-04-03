package xyz.nanian.owl.pitaya.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;

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

    /** 商品ID */
    private Long id;

    /** 分类ID */
    private Long categoryId;

    /** 分类名称（通常需要关联查询出来）*/
    private String categoryName;

    /** 商家ID */
    private Long sellerId;

    /** 商品名称 */
    private String name;

    /** 商品描述*/
    private String description;

    /** 单价 */
    private BigDecimal price;

    /** 库存 */
    private Integer stock;

    /** 封面图URL（主图）*/
    private String coverImg;

    /** 创建时间 */
    private LocalDateTime createTime;

    /** 最后更新时间 */
    private LocalDateTime updateTime;

    List<ProductImageVO> images;

    @Data
    @ToString
    public static class ProductImageVO{

        /**
         * 图片URL
         */
        private String imageUrl;

        /**
         * 图片排序序号 TODO这里与DO名字不同，转换要注意
         */
        private Integer imageSort;
    }

}
