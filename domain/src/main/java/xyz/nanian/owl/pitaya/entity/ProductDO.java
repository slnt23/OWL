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
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "product_code", exist = true)
    private String productCode;

    @TableField(value = "product_name", exist = true)
    private String productName;

    @TableField(value = "category_id", exist = true)
    private Long categoryId;

    @TableField(value = "price", exist = true)
    private BigDecimal price;

    @TableField(value = "stock", exist = true)
    private Integer stock = 0;

    @TableField(value = "status", exist = true)
    private Boolean status = true;

    @TableField(value = "remark", exist = true)
    private String remark;

    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE, exist = true)
    private LocalDateTime updateTime;
}

