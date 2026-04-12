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
 * 价格系统分类表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Getter
@Setter
@TableName("price_category")
@Schema(name = "CategoryDO对象", description = "价格系统分类表")
public class CategoryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(name = "父分类ID（顶级为NULL）")
    @TableField("parent_id")
    private Long parentId;

    @Schema(name = "分类名称")
    @TableField("category_name")
    private String categoryName;

    @Schema(name = "分类唯一编码")
    @TableField("category_code")
    private String categoryCode;

    @Schema(name = "层级（1=一级分类，2=二级分类...）")
    @TableField("level")
    private Integer level;

    @Schema(name = "排序值（越小越靠前）")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(name = "状态：1-启用，0-禁用")
    @TableField("status")
    private Byte status;

    @Schema(name = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;

    @Schema(name = "更新时间")
    @TableField("updated_at")
    private LocalDateTime updatedAt;
}
