package xyz.nanian.owl.caishen.constant;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * 总结周期类型，含区间解析纯逻辑（可单测）。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public enum PeriodType {

    /** 日总结 */
    DAILY,

    /** 周总结 */
    WEEKLY,

    /** 月总结 */
    MONTHLY;

    /**
     * 按名称解析，未知类型抛出 {@link IllegalArgumentException}。
     *
     * @param name 枚举名，null 或空串视为非法
     */
    public static PeriodType fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("periodType 不能为空");
        }
        return PeriodType.valueOf(name.trim().toUpperCase());
    }

    /**
     * 周期统计区间 [start, end]，以 today 为终点。
     *
     * <ul>
     *   <li>DAILY：today ~ today</li>
     *   <li>WEEKLY：本周一 ~ today</li>
     *   <li>MONTHLY：当月 1 号 ~ today</li>
     * </ul>
     */
    public Range resolveRange(LocalDate today) {
        return switch (this) {
            case DAILY -> new Range(today, today);
            case WEEKLY -> new Range(today.with(DayOfWeek.MONDAY), today);
            case MONTHLY -> new Range(today.withDayOfMonth(1), today);
        };
    }

    /**
     * 闭区间。
     */
    public record Range(LocalDate startDate, LocalDate endDate) {
    }
}
