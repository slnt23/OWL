package xyz.nanian.owl.caishen.service.impl;

import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.constant.AlertType;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundAlertDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;

import java.time.LocalDate;

/**
 * 提醒阈值判定纯逻辑，可单测。
 *
 * <p>触发规则：</p>
 * <ul>
 *   <li>RISE_ABOVE + thresholdValue：unitNav &gt;= 阈值</li>
 *   <li>RISE_ABOVE + thresholdPercent：dailyReturnRate &gt;= 阈值（%）</li>
 *   <li>FALL_BELOW + thresholdValue：unitNav &lt;= 阈值</li>
 *   <li>FALL_BELOW + thresholdPercent：dailyReturnRate &lt;= 负的阈值（%）</li>
 * </ul>
 *
 * <p>同一规则同一天不重复触发（last_triggered_at 当天已触发则跳过）。</p>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Component
public class AlertThresholdEvaluator {

    public enum EvalResult {
        /** 触发 */
        TRIGGER,
        /** 同一天已触发，跳过 */
        SKIP_SAME_DAY,
        /** 无净值数据 */
        NO_NAV,
        /** 未达阈值 */
        NOT_MET
    }

    public EvalResult evaluate(CaishenFundAlertDO alert, CaishenFundNavDO nav, LocalDate today) {
        if (nav == null) {
            return EvalResult.NO_NAV;
        }
        if (alert.getLastTriggeredAt() != null
                && alert.getLastTriggeredAt().toLocalDate().equals(today)) {
            return EvalResult.SKIP_SAME_DAY;
        }

        AlertType type = AlertType.valueOf(alert.getAlertType());
        boolean met;
        if (type == AlertType.RISE_ABOVE) {
            if (alert.getThresholdValue() != null) {
                met = nav.getUnitNav() != null
                        && nav.getUnitNav().compareTo(alert.getThresholdValue()) >= 0;
            } else {
                met = nav.getDailyReturnRate() != null
                        && nav.getDailyReturnRate().compareTo(alert.getThresholdPercent()) >= 0;
            }
        } else { // FALL_BELOW
            if (alert.getThresholdValue() != null) {
                met = nav.getUnitNav() != null
                        && nav.getUnitNav().compareTo(alert.getThresholdValue()) <= 0;
            } else {
                met = nav.getDailyReturnRate() != null
                        && nav.getDailyReturnRate().compareTo(alert.getThresholdPercent().negate()) <= 0;
            }
        }
        return met ? EvalResult.TRIGGER : EvalResult.NOT_MET;
    }
}
