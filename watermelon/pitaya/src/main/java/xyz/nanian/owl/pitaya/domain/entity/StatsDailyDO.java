package xyz.nanian.owl.pitaya.domain.entity;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 每日数据统计DO
 *
 * @author slnt23
 * @since 2025/11/12
 */

@Data
@TableName("stats_daily")
@Schema(name = "每日统计表", description = "每日数据统计表")
public class StatsDailyDO {
    @Schema(description = "统计记录ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "统计日期")
    @TableField(value = "stat_date", exist = true)
    private LocalDate statDate;

    @Schema(description = "订单数")
    @TableField(value = "order_count", exist = true)
    private Integer orderCount = 0;

    @Schema(description = "新增用户数")
    @TableField(value = "new_user_count", exist = true)
    private Integer newUserCount = 0;

    @Schema(description = "活跃商品数")
    @TableField(value = "active_product_count", exist = true)
    private Integer activeProductCount = 0;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime createTime;
}