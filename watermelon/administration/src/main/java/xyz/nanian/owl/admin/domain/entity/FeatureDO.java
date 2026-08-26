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
 * 产品特性展示表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@Getter
@Setter
@ToString
@TableName("admin_feature")
@Schema(name = "FeatureDO对象", description = "产品特性展示表")
public class FeatureDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID，唯一标识每条特性
     */
    @Schema(name = "主键ID，唯一标识每条特性")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 图标，可为Font Awesome类名、SVG内容或图片地址
     */
    @TableField("icon")
    @Schema(name = "图标，可为Font Awesome类名、SVG内容或图片地址")
    private String icon;

    /**
     * 特性标题，概括功能或卖点的简短文案
     */
    @TableField("title")
    @Schema(name = "特性标题，概括功能或卖点的简短文案")
    private String title;

    /**
     * 特性详细说明，支持纯文本或Markdown格式
     */
    @TableField("description")
    @Schema(name = "特性详细说明，支持纯文本或Markdown格式")
    private String description;

    /**
     * 排序序号，数值越小越靠前；用于控制特性展示顺序
     */
    @TableField("sort_order")
    @Schema(name = "排序序号，数值越小越靠前；用于控制特性展示顺序")
    private Integer sortOrder;

    /**
     * 记录创建时间
     */
    @TableField("create_time")
    @Schema(name = "创建时间")
    private LocalDateTime createTime;

    /**
     * 记录最后更新时间
     */
    @TableField("update_time")
    @Schema(name = "更新时间")
    private LocalDateTime updateTime;
}
