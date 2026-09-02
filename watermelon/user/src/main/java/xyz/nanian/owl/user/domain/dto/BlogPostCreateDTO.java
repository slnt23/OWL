package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Schema(name = "博客文章创建入参")
public class BlogPostCreateDTO {

    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题最大 255 字符")
    @Schema(description = "文章标题")
    private String title;

    @Size(max = 500, message = "摘要最大 500 字符")
    @Schema(description = "文章摘要")
    private String excerpt;

    @NotBlank(message = "正文不能为空")
    @Schema(description = "文章正文")
    private String content;

    @Schema(description = "封面图片文件")
    private MultipartFile cover;

    @Schema(description = "标签数组，JSON 字符串，如 [\"标签1\",\"标签2\"]")
    private String tags;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}