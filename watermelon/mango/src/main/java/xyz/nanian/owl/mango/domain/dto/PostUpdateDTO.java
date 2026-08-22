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
     * URL 标识，不传则保持原值
     */
    @Schema(description = "URL 标识，不传则保持原值")
    private String slug;

    /**
     * 分类 ID，null 表示清除分类
     */
    @Schema(description = "分类 ID，null 表示清除分类")
    private Long categoryId;

    /**
     * 语言标识
     */
    @Schema(description = "语言标识")
    private String lang;

    /**
     * 阅读时长（分钟），不传则更新正文时按字数重算
     */
    @Schema(description = "阅读时长（分钟），不传则更新正文时按字数重算")
    private Integer readTime;

    /**
     * 关联标签 ID 列表，传入则整体替换
     */
    @Schema(description = "关联标签 ID 列表，传入则整体替换")
    private List<Long> tagIds;

    /**
     * 是否发布
     */
    @Schema(description = "是否发布")
    private Boolean isPublished;

    /**
     * 是否置顶
     */
    @Schema(description = "是否置顶")
    private Boolean isTop;

    /**
     * 发布时间，ISO 8601
     */
    @Schema(description = "发布时间，ISO 8601")
    private LocalDateTime publishTime;
}
