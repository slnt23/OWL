package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
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
public class CategoryDO {

    /**
     * 分类ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 父分类ID，顶级分类为0
     */
    private Long parentId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类层级（1:一级 2:二级 3:三级 ...）
     */
    private Integer level;

    /**
     * 排序字段，值越小越靠前
     */
    private Integer sort;

    /**
     * 分类描述
     */
    private String description;

    /**
     * 是否启用：1=启用，0=禁用
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
