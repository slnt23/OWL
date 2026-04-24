package xyz.nanian.owl.sugarcane.domain.entity;

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
 * 价格文件扩展表
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:35:00
 */
@Getter
@Setter
@ToString
@TableName("price_item_media")
@Schema(name = "PriceItemMediaDO对象", description = "价格文件扩展表")
public class PriceItemMediaDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Schema(name = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 物品ID(关联price_item)
     */
    @TableField("item_id")
    @Schema(name = "物品ID(关联price_item)")
    private Long itemId;

    /**
     * 媒体类型
     */
    @Schema(name = "媒体类型")
    @TableField("media_type")
    private String mediaType;

    /**
     * URL
     */
    @TableField("url")
    @Schema(name = "URL")
    private String url;

    /**
     * 排序
     */
    @Schema(name = "排序")
    @TableField("sort_order")
    private Integer sortOrder;

    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    @TableField("created_at")
    private LocalDateTime createdAt;
}
