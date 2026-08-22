package xyz.nanian.owl.caishen.constant;

/**
 * 提醒规则状态。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public enum AlertStatus {

    /** 生效中 */
    ACTIVE,

    /** 已触发（用户可手动重置为 ACTIVE 继续监控） */
    TRIGGERED,

    /** 暂停 */
    PAUSED
}
