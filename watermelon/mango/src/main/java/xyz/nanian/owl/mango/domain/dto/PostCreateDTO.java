package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 创建文章入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "创建文章入参")
public class PostCreateDTO {

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题最大 200 字符")
    @Schema(description = "标题")
    private String title;

    /**
     * 摘要
     */
    @Size(max = 500, message = "摘要最大 500 字符")
    @Schema(description = "摘要")
    private String excerpt;

    /**
     * Markdown 正文
     */
    @Schema(description = "Markdown 正文")
    private String content;

    /**
     * 封面图 URL
     */
    @Schema(description = "封面图 URL")
    private String coverImage;

    /**
     * URL 标识，不传则自动生成
     */
    @Schema(description = "URL 标识，不传则自动生成")
    private String slug;

    /**
     * 分类 ID
     */
    @Schema(description = "分类 ID")
    private Long categoryId;

    /**
     * 语言标识，默认 zh
     */
    @Schema(description = "语言标识，默认 zh")
    private String lang;

    /**
     * 阅读时长（分钟），不传则按正文字数估算
     */
    @Schema(description = "阅读时长（分钟），不传则按正文字数估算")
    private Integer readTime;

    /**
     * 关联标签 ID 列表
     */
    @Schema(description = "关联标签 ID 列表")
    private List<Long> tagIds;

    /**
     * 是否发布，默认 false（草稿）
     */
    @Schema(description = "是否发布，默认 false（草稿）")
    private Boolean isPublished;

    /**
     * 是否置顶，默认 false
     */
    @Schema(description = "是否置顶，默认 false")
    private Boolean isTop;

    /**
     * 发布时间，ISO 8601
     */
    @Schema(description = "发布时间，ISO 8601")
    private LocalDateTime publishTime;
}
