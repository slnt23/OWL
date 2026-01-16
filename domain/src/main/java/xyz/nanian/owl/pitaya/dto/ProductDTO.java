package xyz.nanian.owl.pitaya.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品模块，
 * 其中时间属性在数据库中会自动的生成以及更改，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "商品DTO",description = "用以查询的DTO")
@Data
public class ProductDTO {

//    @Schema(description = "商品编号",example = "UUID 生成随机码")
//    String productCode;

    @Schema(description = "商品ID",example = "1")
    @NotEmpty
    private Long id;

    @Schema(description = "分类ID",example = "2")
    @NotNull(message = "商品分类不可为空")
    private Long categoryId;

    @Schema(description = "商家ID",example = "2")
    private Long sellerId;

    @Schema(description = "商品名",example = "火龙果")
    private String productName;

    @Schema(description = "商品描述",example = "果肉细腻多汁")
    private String description;

    @Schema(description = "价格",example = "20元一斤")
    @NotNull
    private BigDecimal price;

    @NotNull(message = "库存不可为空")
    @Schema(description = "库存",example = "23")
    private Integer stock;

    @Schema(description = "封面图URL",example = "www.image.com")
    private String coverImage;

    @Schema(description= "状态",example = "1(1:启用  0：禁用)")
    private Integer status;

    @Schema(description = "商品详情图片List")
    @NotEmpty(message = "至少一张图片")
    private List<ProductImageDTO> images;

    @Data
    @Schema(description = "商品详情图片")
    public static class ProductImageDTO{

        @Schema(description = "商品ID",example = "1")
        private Long productId;

        @Schema(description = "图片URL",example = "www.tupian.com")
        @NotNull
        private String imageUrl;

        @Schema(description = "排序号，值越小越靠前",example = "1")
        @Min(value = 0,message = "非负数")
        private Integer sort = 0;
    }
}
