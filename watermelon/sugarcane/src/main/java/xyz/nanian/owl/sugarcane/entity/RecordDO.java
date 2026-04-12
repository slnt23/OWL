package xyz.nanian.owl.sugarcane.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 价格记录表（时间序列数据）
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Getter
@Setter
@TableName("price_record")
@Schema(description = "价格记录表（时间序列数据）")
public class RecordDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID", example = "1")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "物品ID（关联price_item.id）", example = "1001")
    @TableField("item_id")
    private Long itemId;

    @Schema(description = "位置ID（关联geo_location.id）", example = "20001")
    @TableField("location_id")
    private Long locationId;

    @Schema(description = "价格数值", example = "7.85")
    @TableField("price")
    private BigDecimal price;

    @Schema(description = "币种（CNY-人民币，USD-美元）", example = "CNY")
    @TableField("currency")
    private String currency;

    @Schema(description = "价格单位（如：元/升、美元/吨）", example = "元/升")
    @TableField("price_unit")
    private String priceUnit;

    @Schema(description = "来源ID（关联price_source.id）", example = "3001")
    @TableField("source_id")
    private Long sourceId;

    @Schema(description = "生效时间（价格开始生效的时间点）", example = "2025-04-12T08:00:00")
    @TableField("effective_time")
    private LocalDateTime effectiveTime;

    @Schema(description = "失效时间（NULL表示长期有效）", example = "2025-12-31T23:59:59")
    @TableField("expire_time")
    private LocalDateTime expireTime;

    @Schema(description = "记录时间（数据录入系统的时间）", example = "2025-04-12T10:30:00")
    @TableField("record_time")
    private LocalDateTime recordTime;

    @Schema(description = "可信度（0-100，数值越高越可信）", example = "85.5")
    @TableField("confidence")
    private BigDecimal confidence;

    @Schema(description = "创建时间", example = "2025-04-12T10:30:00")
    @TableField("created_at")
    private LocalDateTime createdAt;
}
