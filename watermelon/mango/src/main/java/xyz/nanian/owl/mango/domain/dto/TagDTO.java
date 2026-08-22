package xyz.nanian.owl.mango.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 标签创建/更新入参
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "标签创建/更新入参")
public class TagDTO {

    /**
     * 标签名称
     */
    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称最大 50 字符")
    @Schema(description = "标签名称")
    private String name;

    /**
     * URL 标识
     */
    @Schema(description = "URL 标识")
    private String slug;
}
