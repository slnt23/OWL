package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 画廊DTO
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Data
@Schema(name = "画廊DTO")
public class GalleryDTO {

    @Schema(description = "ID，新增时不传，修改时必传", example = "1")
    private Long id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100个字符")
    @Schema(description = "标题", example = "示例图片", maxLength = 100)
    private String title;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Schema(description = "描述", example = "这是一张示例图片", maxLength = 500)
    private String description;

    @Schema(type = "string", format = "binary", description = "大图文件")
    private MultipartFile image;

    @Schema(type = "string", format = "binary", description = "缩略图文件")
    private MultipartFile thumbnail;

    @NotNull(message = "排序序号不能为空")
    @Min(value = 0, message = "排序序号不能小于0")
    @Max(value = 9999, message = "排序序号不能大于9999")
    @Schema(description = "排序序号", example = "1", minimum = "0", maximum = "9999")
    private Integer sortOrder;
}