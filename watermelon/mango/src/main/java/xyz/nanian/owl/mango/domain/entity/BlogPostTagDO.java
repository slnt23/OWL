package xyz.nanian.owl.mango.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 博客文章-标签关联实体类，对应数据库表：blog_post_tag
 * 自增 id 主键，(post_id, tag_id) 唯一约束保证不重复。
 * 关联表仅用于插入、删除与查询，不提供按主键单条操作。
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_post_tag")
@Schema(name = "文章标签关联表", description = "文章标签关联表")
public class BlogPostTagDO {

    @Schema(description = "主键ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "文章ID")
    @TableField("post_id")
    private Long postId;

    @Schema(description = "标签ID")
    @TableField("tag_id")
    private Long tagId;
}