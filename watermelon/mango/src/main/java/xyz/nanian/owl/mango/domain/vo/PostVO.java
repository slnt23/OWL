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

    @Schema(description = "文章 ID")
    private Long id;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String excerpt;

    @Schema(description = "URL 标识")
    private String slug;

    @Schema(description = "封面图")
    private String coverImage;

    @Schema(description = "分类 ID")
    private Long categoryId;

    @Schema(description = "分类名称")
    private String categoryName;

    @Schema(description = "标签列表")
    private List<TagVO> tags;

    @Schema(description = "语言")
    private String lang;

    @Schema(description = "阅读时长（分钟）")
    private Integer readTime;

    @Schema(description = "是否已发布")
    private Boolean isPublished;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "浏览次数")
    private Integer viewCount;

    @Schema(description = "点赞数")
    private Integer likeCount;

    @Schema(description = "发布时间")
    private LocalDateTime publishTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}