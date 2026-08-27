package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品分类实体
 *
 * @author slnt23
 * @since 2026/1/14
 */

@Data
@TableName("category")
@Schema(name = "商品分类表", description = "商品分类表")
public class CategoryDO {

    @Schema(description = "分类ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "父分类ID，顶级分类为0")
    private Long parentId;

    @Schema(description = "分类名称")
    private String name;

    @Schema(description = "分类层级（1:一级 2:二级 3:三级 ...）")
    private Integer level;

    @Schema(description = "排序字段，值越小越靠前")
    private Integer sort;

    @Schema(description = "分类描述")
    private String description;

    @Schema(description = "是否启用：1=启用，0=禁用")
    private Boolean enabled;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}