package xyz.nanian.owl.sugarcane.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 被定价物品表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Getter
@Setter
@TableName("price_item")
@Schema(description = "被定价物品表")
public class ItemDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "物品唯一编码（系统内部识别）", example = "ITEM-20250412001")
    @TableField("item_code")
    private String itemCode;

    @Schema(description = "物品名称", example = "95号汽油")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "分类ID（关联price_category.id）", example = "1001")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "计价单位（如：升、克、吨、次）", example = "升")
    @TableField("unit")
    private String unit;

    @Schema(description = "规格描述（如：500ml装）", example = "500ml装")
    @TableField("specification")
    private String specification;

    @Schema(description = "备注说明", example = "适用于小型车辆")
    @TableField("description")
    private String description;

    @Schema(description = "关联商城商品ID（可为空）", example = "50023")
    @TableField("ref_product_id")
    private Long refProductId;

    @Schema(description = "状态：1-启用，0-停用", example = "1")
    @TableField("status")
    private Byte status;

    @Schema(description = "创建时间", example = "2025-04-12T08:30:00")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间", example = "2025-04-12T10:15:00")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
