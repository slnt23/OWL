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
 * 博客文章实体类，对应数据库表：blog_post
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blog_post")
public class BlogPostDO {

    /**
     * 文章ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String excerpt;

    /**
     * 正文（Markdown 原文）
     */
    private String content;

    /**
     * 封面图 URL
     */
    @TableField("cover_image")
    private String coverImage;

    /**
     * URL 友好标识
     */
    private String slug;

    /**
     * 所属分类ID
     */
    @TableField("category_id")
    private Long categoryId;

    /**
     * 语言标识：zh / en
     */
    private String lang;

    /**
     * 预估阅读时长（分钟）
     */
    @TableField("read_time")
    private Integer readTime;

    /**
     * 是否发布：0=草稿，1=已发布
     */
    @TableField("is_published")
    private Integer isPublished;

    /**
     * 是否置顶：0=否，1=是
     */
    @TableField("is_top")
    private Integer isTop;

    /**
     * 浏览次数
     */
    @TableField("view_count")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @TableField("like_count")
    private Integer likeCount;

    /**
     * 发布时间
     */
    @TableField("publish_time")
    private LocalDateTime publishTime;

    /**
     * 作者用户ID
     */
    @TableField("created_by")
    private Long createdBy;

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
