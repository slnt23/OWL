package xyz.nanian.owl.sugarcane.domain.dto;


import lombok.Data;
import xyz.nanian.owl.sugarcane.constant.QueryType;
import xyz.nanian.owl.sugarcane.constant.TimeGranularity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 价格查询DTO，这里这个不用，是综合，已拆开
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class PriceQueryDTO {

    // 核心条件
    private Long itemId;
    private String itemCode;
    private List<Long> itemIds;

    private Long locationId;
    private List<Long> locationIds;

    // 时间范围
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // 查询模式（关键）
    private QueryType queryType;
    // LATEST / TREND / COMPARE_LOCATION / COMPARE_SOURCE

    // 来源筛选
    private List<Long> sourceIds;
    private Integer minReliability;

    // 其他过滤
    private String currency;
    private BigDecimal minConfidence;

    // 聚合粒度（趋势用）
    private TimeGranularity granularity;
    // HOUR / DAY / WEEK / MONTH

    // 是否只取最新一条（优化用）
    private Boolean latestOnly = false;

    // 分页
    private Integer pageNo = 1;
    private Integer pageSize = 20;
}
