package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品图片
 *
 * @author slnt23
 * @since 2026/1/14
 */


@Data
@TableName("product_image")
@Schema(name = "商品图片表", description = "商品图片表")
public class ProductImageDO {

    @Schema(description = "图片ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "商品ID")
    private Long productId;

    @Schema(description = "图片URL")
    private String imageUrl;

    @Schema(description = "排序")
    private Integer sort;
}