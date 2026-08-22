package xyz.nanian.owl.caishen.constant;

/**
 * 总结任务状态机。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public enum SummaryStatus {

    /** 待处理 */
    PENDING,

    /** 生成中 */
    PROCESSING,

    /** 成功 */
    SUCCESS,

    /** 失败 */
    FAILED
}
