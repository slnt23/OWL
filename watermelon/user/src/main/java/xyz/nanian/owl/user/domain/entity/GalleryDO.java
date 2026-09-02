package xyz.nanian.owl.user.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 画廊表
 * </p>
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Getter
@Setter
@ToString
@TableName("user_gallery")
@Schema(name = "GalleryDO对象", description = "画廊表")
public class GalleryDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "标题")
    @TableField("title")
    private String title;

    @Schema(description = "描述")
    @TableField("description")
    private String description;

    @Schema(description = "大图URL")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "缩略图URL")
    @TableField("thumbnail_url")
    private String thumbnailUrl;

    @Schema(description = "排序序号，数值越小越靠前")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}