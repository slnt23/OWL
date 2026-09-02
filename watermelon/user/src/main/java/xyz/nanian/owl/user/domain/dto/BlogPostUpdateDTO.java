package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(name = "博客文章更新入参")
public class BlogPostUpdateDTO {

    @Schema(description = "文章ID")
    private Long id;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章摘要")
    private String excerpt;

    @Schema(description = "文章正文")
    private String content;

    @Schema(description = "封面图片文件")
    private MultipartFile cover;

    @Schema(description = "标签数组，JSON 字符串")
    private String tags;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}