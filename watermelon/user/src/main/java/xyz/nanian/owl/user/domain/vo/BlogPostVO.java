package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "博客文章 VO")
public class BlogPostVO {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章摘要")
    private String excerpt;

    @Schema(description = "文章正文")
    private String content;

    @Schema(description = "封面图片URL")
    private String coverUrl;

    @Schema(description = "标签数组")
    private List<String> tags;

    @Schema(description = "状态：0=草稿，1=已发布")
    private Integer status;

    @Schema(description = "排序权重")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}