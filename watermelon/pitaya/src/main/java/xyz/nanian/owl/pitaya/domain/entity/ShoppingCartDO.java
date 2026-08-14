package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车DO
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Data
@TableName(value = "cart_item")
public class ShoppingCartDO {

    @TableId("id")
    Long cartId;

    @TableField("user_id")
    Long userId;

    @TableField("product_id")
    Long productId;

    @TableField("quantity")
    Integer quantity;

    @TableField(value = "checked")
    Integer checked;

    @TableField(value = "create_time")
    LocalDateTime createdTime;

    /**
     * 这是更新时间，但是数据库我设置
     *  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     *  所以这里到底要不要？目前保留
     */
    @TableField(value = "update_time")
    LocalDateTime updatedTime;
}
