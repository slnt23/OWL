package xyz.nanian.owl.user.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_skill_item")
@Schema(name = "博客技能项表")
public class BlogSkillItemDO {

    @Schema(description = "条目ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属分类ID")
    @TableField("category_id")
    private Long categoryId;

    @Schema(description = "技能名称")
    @TableField("item_name")
    private String itemName;

    @Schema(description = "排序权重")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}