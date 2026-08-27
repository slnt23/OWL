package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "购物车表", description = "购物车表")
public class ShoppingCartDO {

    @Schema(description = "购物车项ID")
    @TableId("id")
    Long cartId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    Long userId;

    @Schema(description = "商品ID")
    @TableField("product_id")
    Long productId;

    @Schema(description = "数量")
    @TableField("quantity")
    Integer quantity;

    @Schema(description = "是否选中")
    @TableField(value = "checked")
    Integer checked;

    @Schema(description = "创建时间")
    @TableField(value = "create_time")
    LocalDateTime createdTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time")
    LocalDateTime updatedTime;
}