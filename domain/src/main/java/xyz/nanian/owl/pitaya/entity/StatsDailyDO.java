package xyz.nanian.owl.pitaya.entity;


import com.baomidou.mybatisplus.annotation.*;
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
public class StatsDailyDO {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField(value = "stat_date", exist = true)
    private LocalDate statDate;

    @TableField(value = "order_count", exist = true)
    private Integer orderCount = 0;

    @TableField(value = "new_user_count", exist = true)
    private Integer newUserCount = 0;

    @TableField(value = "active_product_count", exist = true)
    private Integer activeProductCount = 0;

    @TableField(value = "create_time", fill = FieldFill.INSERT, exist = true)
    private LocalDateTime createTime;
}

