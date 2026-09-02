package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 画廊VO
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Data
@Schema(name = "画廊VO")
public class GalleryVO {

    @Schema(description = "画廊项ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "标题", example = "示例图片")
    private String title;

    @Schema(description = "描述", example = "这是一张示例图片")
    private String description;

    @Schema(description = "大图URL")
    private String imageUrl;

    @Schema(description = "缩略图URL")
    private String thumbnailUrl;

    @Schema(description = "排序序号，数值越小越靠前", example = "1")
    private Integer sortOrder;
}