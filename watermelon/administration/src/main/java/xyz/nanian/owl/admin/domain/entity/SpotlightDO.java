package xyz.nanian.owl.admin.domain.entity;

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
 * 首页焦点展示项目表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@Getter
@Setter
@ToString
@TableName("admin_spotlight")
@Schema(name = "SpotlightDO对象", description = "首页焦点展示项目表")
public class SpotlightDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID，唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "眉题/前置标题，通常为短标签或引导文字")
    @TableField("eyebrow")
    private String eyebrow;

    @Schema(description = "主标题，焦点项目的核心文案")
    @TableField("title")
    private String title;

    @Schema(description = "详细描述，可包含HTML格式或纯文本")
    @TableField("description")
    private String description;

    @Schema(description = "配图，存储相对路径或CDN完整URL")
    @TableField("image_url")
    private String imageUrl;

    @Schema(description = "排序序号，数值越小越靠前")
    @TableField("sort_order")
    private Integer sortOrder;

    @Schema(description = "点击跳转链接，可为内部路由或外部URL")
    @TableField("link")
    private String link;

    @Schema(description = "链接打开方式：_self当前页、_blank新标签页、_parent父框架")
    @TableField("target")
    private String target;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @TableField("update_time")
    private LocalDateTime updateTime;
}