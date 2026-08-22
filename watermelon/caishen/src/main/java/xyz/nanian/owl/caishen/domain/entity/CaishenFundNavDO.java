package xyz.nanian.owl.caishen.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 基金净值历史（追加型，幂等 upsert）。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@TableName("caishen_fund_nav")
public class CaishenFundNavDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 基金代码 */
    @TableField("fund_code")
    private String fundCode;

    /** 净值日期 */
    @TableField("nav_date")
    private LocalDate navDate;

    /** 单位净值 */
    @TableField("unit_nav")
    private BigDecimal unitNav;

    /** 累计净值 */
    @TableField("accumulated_nav")
    private BigDecimal accumulatedNav;

    /** 日收益率（%），如 1.2345 表示 1.2345% */
    @TableField("daily_return_rate")
    private BigDecimal dailyReturnRate;

    /** 数据来源：mock/python/manual */
    private String source;

    @TableField("create_time")
    private LocalDateTime createTime;
}
