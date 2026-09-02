package xyz.nanian.owl.user.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_post")
@Schema(name = "博客文章表")
public class BlogPostDO {

    @Schema(description = "文章ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "所属用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章摘要")
    private String excerpt;

    @Schema(description = "文章正文")
    private String content;

    @Schema(description = "封面图片URL")
    @TableField("cover_url")
    private String coverUrl;

    @Schema(description = "标签数组（JSON）")
    @TableField("tags")
    private String tags;

    @Schema(description = "状态：0=草稿，1=已发布")
    private Integer status;

    @Schema(description = "排序权重")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除：0=未删除，1=已删除")
    @TableLogic
    private Integer deleted;
}