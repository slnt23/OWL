package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 更新文章入参，所有字段可选，仅更新传入字段
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "更新文章入参")
public class PostUpdateDTO {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "摘要")
    private String excerpt;

    @Schema(description = "Markdown 正文")
    private String content;

    @Schema(description = "封面图 URL")
    private String coverImage;

    @Schema(description = "URL 标识，不传则保持原值")
    private String slug;

    @Schema(description = "分类 ID，null 表示清除分类")
    private Long categoryId;

    @Schema(description = "语言标识")
    private String lang;

    @Schema(description = "阅读时长（分钟），不传则更新正文时按字数重算")
    private Integer readTime;

    @Schema(description = "关联标签 ID 列表，传入则整体替换")
    private List<Long> tagIds;

    @Schema(description = "是否发布")
    private Boolean isPublished;

    @Schema(description = "是否置顶")
    private Boolean isTop;

    @Schema(description = "发布时间，ISO 8601")
    private LocalDateTime publishTime;
}