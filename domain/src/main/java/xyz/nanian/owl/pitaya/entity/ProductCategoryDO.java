package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品明细表DO
 *
 * @author slnt23
 * @since 2025/11/12
 */


@Data
@TableName("product_category")
public class ProductCategoryDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "category_code")
    private String categoryCode;

    @TableField(value = "category_name")
    private String categoryName;

    @TableField(value = "description")
    private String description;

    @TableField(value = "enabled")
    private Boolean enabled = true;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
