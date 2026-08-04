package xyz.nanian.owl.admin.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


/**
 * 焦点展示DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */
@Data
@Schema(description = "焦点展示DTO")
public class SpotlightDTO {

    /** 新增时不传，修改时必传 */
    @Schema(description = "ID，新增时不传，修改时必传")
    private Integer id;

    @NotBlank(message = "眉题不能为空")
    @Size(max = 50, message = "眉题长度不能超过50个字符")
    @Schema(description = "眉题", example = "热门推荐", maxLength = 50)
    private String eyebrow;

    @NotBlank(message = "主标题不能为空")
    @Size(max = 100, message = "主标题长度不能超过100个字符")
    @Schema(description = "主标题", example = "这是一个焦点标题", maxLength = 100)
    private String title;

    @Size(max = 500, message = "描述长度不能超过500个字符")
    @Schema(description = "描述", example = "这是焦点的详细描述信息", maxLength = 500)
    private String description;

    @Schema(type = "string", format = "binary", description = "图片文件")
    @NotNull(message = "图片不能为空")
    private MultipartFile image;

    @NotNull(message = "排序序号不能为空")
    @Min(value = 0, message = "排序序号不能小于0")
    @Max(value = 9999, message = "排序序号不能大于9999")
    @Schema(description = "排序序号", example = "1", minimum = "0", maximum = "9999")
    private Integer order;

    @Size(max = 255, message = "跳转链接长度不能超过255个字符")
    @Schema(description = "跳转链接", example = "https://example.com", maxLength = 255)
    private String link;

    @Size(max = 255, message = "目标地址长度不能超过255个字符")
    @Schema(description = "打开方式", example = "_blank")
    private String target;

}
