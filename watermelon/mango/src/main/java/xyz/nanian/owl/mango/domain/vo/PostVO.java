package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章列表项 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "文章列表项 VO")
public class PostVO {

    /**
     * 文章 ID
     */
    @Schema(description = "文章 ID")
    private Long id;

    /**
     * 标题
     */
    @Schema(description = "标题")
    private String title;

    /**
     * 摘要
     */
    @Schema(description = "摘要")
    private String excerpt;

    /**
     * URL 标识
     */
    @Schema(description = "URL 标识")
    private String slug;

    /**
     * 封面图
     */
    @Schema(description = "封面图")
    private String coverImage;

    /**
     * 分类 ID
     */
    @Schema(description = "分类 ID")
    private Long categoryId;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String categoryName;

    /**
     * 标签列表
     */
    @Schema(description = "标签列表")
    private List<TagVO> tags;

    /**
     * 语言
     */
    @Schema(description = "语言")
    private String lang;

    /**
     * 阅读时长（分钟）
     */
    @Schema(description = "阅读时长（分钟）")
    private Integer readTime;

    /**
     * 是否已发布
     */
    @Schema(description = "是否已发布")
    private Boolean isPublished;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean isTop;

    /**
     * 浏览次数
     */
    @Schema(description = "浏览次数")
    private Integer viewCount;

    /**
     * 点赞数
     */
    @Schema(description = "点赞数")
    private Integer likeCount;

    /**
     * 发布时间
     */
    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
