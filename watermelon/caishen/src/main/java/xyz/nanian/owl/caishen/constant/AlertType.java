package xyz.nanian.owl.caishen.constant;

/**
 * 提醒类型。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public enum AlertType {

    /** 涨到（最新净值或日收益率 >= 阈值） */
    RISE_ABOVE,

    /** 跌到（最新净值或日收益率 <= 阈值） */
    FALL_BELOW;

    /**
     * 按名称解析，未知类型抛出 {@link IllegalArgumentException}。
     *
     * @param name 枚举名，null 或空串视为非法
     */
    public static AlertType fromName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("alertType 不能为空");
        }
        return AlertType.valueOf(name);
    }
}
