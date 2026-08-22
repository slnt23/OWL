package xyz.nanian.owl.mango.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 博客技能条目实体类，对应数据库表：blog_skill_item
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_skill_item")
public class BlogSkillItemDO {

    /**
     * 条目ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属技能分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 技能名称
     */
    private String name;

    /**
     * 分类内排序序号
     */
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
