package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品DO
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("product")
public class ProductDO {
    // 商品ID
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "category_id")
    private Long categoryId;

    @TableField(value = "seller_id")
    private Long sellerId;

    @TableField(value = "name")
    private String name;

    @TableField(value = "description")
    private String description;

    @TableField(value = "price")
    private BigDecimal price;

    @TableField(value = "stock")
    private Integer stock;

    @TableField(value = "status")
    private Integer status = 0;

    @TableField(value = "cover_img")
    private String coverImg;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(value = "update_time")
    private LocalDateTime updateTime;
}


