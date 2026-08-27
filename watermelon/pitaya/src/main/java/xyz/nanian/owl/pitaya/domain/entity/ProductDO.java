package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品DO，用于用户在总的搜索栏中搜索商品，
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("product")
@Schema(name = "商品表", description = "商品信息表")
public class ProductDO {
    @Schema(description = "商品ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "分类ID")
    @TableField(value = "category_id")
    private Long categoryId;

    @Schema(description = "商家ID")
    @TableField(value = "seller_id")
    private Long sellerId;

    @Schema(description = "商品名称")
    @TableField(value = "name")
    private String name;

    @Schema(description = "商品描述")
    @TableField(value = "description")
    private String description;

    @Schema(description = "商品单价")
    @TableField(value = "price")
    private BigDecimal price;

    @Schema(description = "库存数量")
    @TableField(value = "stock")
    private Integer stock;

    @Schema(description = "状态（1:启用 0:禁用）")
    @TableField(value = "status")
    private Integer status = 0;

    @Schema(description = "封面图URL")
    @TableField(value = "cover_img")
    private String coverImg;

    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}