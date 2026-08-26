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

    /**
     * 主键ID，唯一标识
     */
    @Schema(name = "主键ID，唯一标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 眉题/前置标题，通常为短标签或引导文字
     */
    @TableField("eyebrow")
    @Schema(name = "眉题/前置标题，通常为短标签或引导文字")
    private String eyebrow;

    /**
     * 主标题，焦点项目的核心文案
     */
    @TableField("title")
    @Schema(name = "主标题，焦点项目的核心文案")
    private String title;

    /**
     * 详细描述，可包含HTML格式或纯文本
     */
    @TableField("description")
    @Schema(name = "详细描述，可包含HTML格式或纯文本")
    private String description;

    /**
     * 配图，存储相对路径或CDN完整URL
     */
    @TableField("image_url")
    @Schema(name = "配图，存储相对路径或CDN完整URL")
    private String imageUrl;

    /**
     * 排序序号，数学越小越靠前；同数值按创建时间排序
     */
    @TableField("sort_order")
    @Schema(name = "排序序号，数值越小越靠前")
    private Integer sortOrder;

    /**
     * 点击跳转链接，可为内部路由或外部URL
     */
    @TableField("link")
    @Schema(name = "点击跳转链接，可为内部路由或外部URL")
    private String link;

    /**
     * 链接打开方式：_self当前页、_blank新标签页、_parent父框架
     */
    @TableField("target")
    @Schema(name = "链接打开方式：_self当前页、_blank新标签页、_parent父框架")
    private String target;

    /**
     * 创建时间，行首次插入时自动设置
     */
    @TableField("create_time")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间，行每次修改时自动刷新
     */
    @TableField("update_time")
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
}
