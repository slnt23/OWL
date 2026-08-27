package xyz.nanian.owl.caishen.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.caishen.constant.AlertStatus;
import xyz.nanian.owl.caishen.constant.AlertType;
import xyz.nanian.owl.caishen.domain.dto.FundAlertCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundAlertUpdateDTO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundAlertDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenUserEmailDO;
import xyz.nanian.owl.caishen.domain.vo.FundAlertVO;
import xyz.nanian.owl.caishen.mapper.CaishenFundAlertMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundNavMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundWatchMapper;
import xyz.nanian.owl.caishen.mapper.CaishenUserEmailMapper;
import xyz.nanian.owl.caishen.mapstruct.CaishenFundAlertConvert;
import xyz.nanian.owl.caishen.service.AlertService;
import xyz.nanian.owl.caishen.service.impl.AlertThresholdEvaluator.EvalResult;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.mail.MailMessage;
import xyz.nanian.owl.common.mail.MailService;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.log.annotation.OperationLog;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 提醒规则 CRUD + 阈值检查 + 邮件触发实现。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

    private final CaishenFundAlertMapper caishenFundAlertMapper;
    private final CaishenFundWatchMapper caishenFundWatchMapper;
    private final CaishenFundMapper caishenFundMapper;
    private final CaishenFundNavMapper caishenFundNavMapper;
    private final CaishenUserEmailMapper caishenUserEmailMapper;
    private final CaishenFundAlertConvert caishenFundAlertConvert;
    private final AlertThresholdEvaluator alertThresholdEvaluator;
    private final MailService mailService;

    @Value("${owl.mail.alert.subject}")
    private String alertSubjectTemplate;

    @Value("${owl.mail.alert.body}")
    private String alertBodyTemplate;

    @Override
    public List<FundAlertVO> listByWatch(Long watchId) {
        Long userId = requireUserId();
        CaishenFundWatchDO watch = requireOwnedWatch(watchId, userId);
        List<CaishenFundAlertDO> alerts = caishenFundAlertMapper.selectList(
                Wrappers.<CaishenFundAlertDO>lambdaQuery()
                        .eq(CaishenFundAlertDO::getWatchId, watchId)
                        .orderByDesc(CaishenFundAlertDO::getCreateTime));
        List<FundAlertVO> vos = caishenFundAlertConvert.toVOList(alerts);
        String fundName = fundName(watch.getFundCode());
        for (FundAlertVO vo : vos) {
            vo.setFundCode(watch.getFundCode());
            vo.setFundName(fundName);
        }
        return vos;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "新增提醒规则")
    public Long create(Long watchId, FundAlertCreateDTO dto) {
        Long userId = requireUserId();
        requireOwnedWatch(watchId, userId);

        AlertType type;
        try {
            type = AlertType.fromName(dto.getAlertType());
        } catch (IllegalArgumentException e) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
        boolean hasValue = dto.getThresholdValue() != null;
        boolean hasPercent = dto.getThresholdPercent() != null;
        if (hasValue == hasPercent) {
            // 恰好提供一个阈值（都有或都没有都非法）
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }

        LocalDateTime now = LocalDateTime.now();
        CaishenFundAlertDO alert = new CaishenFundAlertDO();
        alert.setWatchId(watchId);
        alert.setAlertType(type.name());
        alert.setThresholdValue(hasValue ? dto.getThresholdValue() : null);
        alert.setThresholdPercent(hasPercent ? dto.getThresholdPercent() : null);
        alert.setStatus(AlertStatus.ACTIVE.name());
        alert.setCreateTime(now);
        alert.setUpdateTime(now);
        caishenFundAlertMapper.insert(alert);
        return alert.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "更新提醒规则")
    public void update(Long id, FundAlertUpdateDTO dto) {
        OwnedAlert owned = requireOwnedAlert(id);
        CaishenFundAlertDO alert = owned.alert();

        LambdaUpdateWrapper<CaishenFundAlertDO> wrapper = Wrappers.<CaishenFundAlertDO>lambdaUpdate()
                .eq(CaishenFundAlertDO::getId, id);
        boolean changed = false;

        if (dto.getAlertType() != null) {
            try {
                AlertType.fromName(dto.getAlertType());
            } catch (IllegalArgumentException e) {
                throw new BizException(ResultStatus.PARAMS_INVALID);
            }
            wrapper.set(CaishenFundAlertDO::getAlertType, dto.getAlertType());
            changed = true;
        }
        if (dto.getStatus() != null) {
            String status = dto.getStatus().toUpperCase();
            if (!AlertStatus.ACTIVE.name().equals(status) && !AlertStatus.PAUSED.name().equals(status)) {
                throw new BizException(ResultStatus.PARAMS_INVALID);
            }
            wrapper.set(CaishenFundAlertDO::getStatus, status);
            changed = true;
        }
        boolean valueCleared = dto.getThresholdValue() != null;
        boolean percentCleared = dto.getThresholdPercent() != null;
        if (valueCleared) {
            // 设置 value 则清除 percent，保持恰好一个阈值
            wrapper.set(CaishenFundAlertDO::getThresholdValue, dto.getThresholdValue())
                    .set(CaishenFundAlertDO::getThresholdPercent, null);
            changed = true;
        } else if (percentCleared) {
            wrapper.set(CaishenFundAlertDO::getThresholdPercent, dto.getThresholdPercent())
                    .set(CaishenFundAlertDO::getThresholdValue, null);
            changed = true;
        }

        // 兜底校验：更新后仍须至少一个阈值
        BigDecimal finalValue = valueCleared ? null
                : (percentCleared ? null : alert.getThresholdValue());
        BigDecimal finalPercent = percentCleared ? null
                : (valueCleared ? null : alert.getThresholdPercent());
        if (finalValue == null && finalPercent == null) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }

        if (changed) {
            wrapper.set(CaishenFundAlertDO::getUpdateTime, LocalDateTime.now());
            caishenFundAlertMapper.update(null, wrapper);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "删除提醒规则")
    public void delete(Long id) {
        requireOwnedAlert(id);
        caishenFundAlertMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "重置提醒规则")
    public void reset(Long id) {
        requireOwnedAlert(id);
        // 清空 last_triggered_at，允许同日重新武装
        caishenFundAlertMapper.update(null, Wrappers.<CaishenFundAlertDO>lambdaUpdate()
                .eq(CaishenFundAlertDO::getId, id)
                .set(CaishenFundAlertDO::getStatus, AlertStatus.ACTIVE.name())
                .set(CaishenFundAlertDO::getLastTriggeredAt, null)
                .set(CaishenFundAlertDO::getUpdateTime, LocalDateTime.now()));
    }

    @Override
    public void checkAndTrigger(LocalDate today) {
        List<CaishenFundAlertDO> activeAlerts = caishenFundAlertMapper.selectList(
                Wrappers.<CaishenFundAlertDO>lambdaQuery()
                        .eq(CaishenFundAlertDO::getStatus, AlertStatus.ACTIVE.name()));
        for (CaishenFundAlertDO alert : activeAlerts) {
            try {
                checkOne(alert, today);
            } catch (Exception e) {
                // 单条规则异常不中断批次，下次调度重试
                log.error("[caishen] 检查提醒规则失败 alertId={}", alert.getId(), e);
            }
        }
    }

    private void checkOne(CaishenFundAlertDO alert, LocalDate today) {
        CaishenFundWatchDO watch = caishenFundWatchMapper.selectById(alert.getWatchId());
        if (watch == null) {
            return;
        }
        CaishenFundNavDO latestNav = caishenFundNavMapper.selectOne(
                Wrappers.<CaishenFundNavDO>lambdaQuery()
                        .eq(CaishenFundNavDO::getFundCode, watch.getFundCode())
                        .orderByDesc(CaishenFundNavDO::getNavDate)
                        .last("LIMIT 1"));
        if (alertThresholdEvaluator.evaluate(alert, latestNav, today) != EvalResult.TRIGGER) {
            return;
        }

        String fundName = fundName(watch.getFundCode());
        CaishenUserEmailDO userEmail = caishenUserEmailMapper.selectById(watch.getUserId());
        if (userEmail == null || userEmail.getEmail() == null || userEmail.getEmail().isBlank()) {
            // 无邮箱无法送达，标记已触发避免每日重复判定，并记录告警
            log.warn("[caishen] 用户 {} 无邮箱，提醒规则 {} 标记已触发", watch.getUserId(), alert.getId());
            markTriggered(alert);
            return;
        }

        // String subject = "【OWL 理财提醒】基金 " + watch.getFundCode() + "（" + safe(fundName) + "）净值触及阈值";
        String subject = alertSubjectTemplate
                .replace("{fundCode}", watch.getFundCode())
                .replace("{fundName}", safe(fundName));
        String body = buildAlertBody(watch, latestNav, alert, fundName);
        mailService.send(MailMessage.builder()
                .to(userEmail.getEmail())
                .subject(subject)
                .body(body)
                .html(true)
                .build());
        // 发送成功后才置 TRIGGERED；发送失败保持 ACTIVE，下次调度重试
        markTriggered(alert);
    }

    private void markTriggered(CaishenFundAlertDO alert) {
        caishenFundAlertMapper.update(null, Wrappers.<CaishenFundAlertDO>lambdaUpdate()
                .eq(CaishenFundAlertDO::getId, alert.getId())
                .set(CaishenFundAlertDO::getStatus, AlertStatus.TRIGGERED.name())
                .set(CaishenFundAlertDO::getLastTriggeredAt, LocalDateTime.now())
                .set(CaishenFundAlertDO::getUpdateTime, LocalDateTime.now()));
    }

    private String buildAlertBody(CaishenFundWatchDO watch, CaishenFundNavDO nav,
                                  CaishenFundAlertDO alert, String fundName) {
        String typeLabel = AlertType.RISE_ABOVE.name().equals(alert.getAlertType()) ? "涨到" : "跌到";
        String thresholdText = alert.getThresholdValue() != null
                ? alert.getThresholdValue().toPlainString()
                : alert.getThresholdPercent().toPlainString() + "%";
        // return "<p>您关注的基金「" + safe(fundName) + "（" + watch.getFundCode() + "）」已触及您设置的提醒阈值：</p>"
        //         + "<ul>"
        //         + "<li>提醒类型：" + typeLabel + "</li>"
        //         + "<li>阈值：" + thresholdText + "</li>"
        //         + "<li>当前净值：" + nav.getUnitNav() + "（" + nav.getNavDate() + "）</li>"
        //         + "<li>日收益率：" + nav.getDailyReturnRate() + "%</li>"
        //         + "</ul>"
        //         + "<p>请登录查看详情。</p>";
        return alertBodyTemplate
                .replace("{fundName}", safe(fundName))
                .replace("{fundCode}", watch.getFundCode())
                .replace("{typeLabel}", typeLabel)
                .replace("{thresholdText}", thresholdText)
                .replace("{unitNav}", nav.getUnitNav() != null ? nav.getUnitNav().toPlainString() : "-")
                .replace("{navDate}", nav.getNavDate() != null ? nav.getNavDate().toString() : "-")
                .replace("{dailyReturnRate}", nav.getDailyReturnRate() != null ? nav.getDailyReturnRate().toPlainString() : "-");
    }

    private String fundName(String fundCode) {
        CaishenFundDO fund = caishenFundMapper.selectOne(
                Wrappers.<CaishenFundDO>lambdaQuery().eq(CaishenFundDO::getFundCode, fundCode));
        return fund == null ? null : fund.getFundName();
    }

    private Long requireUserId() {
        Long userId = CurrentUserContext.getUserId();
        if (userId == null) {
            throw new BizException(ResultStatus.UNAUTHORIZED);
        }
        return userId;
    }

    private CaishenFundWatchDO requireOwnedWatch(Long watchId, Long userId) {
        CaishenFundWatchDO watch = caishenFundWatchMapper.selectOne(
                Wrappers.<CaishenFundWatchDO>lambdaQuery()
                        .eq(CaishenFundWatchDO::getId, watchId)
                        .eq(CaishenFundWatchDO::getUserId, userId));
        if (watch == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return watch;
    }

    private OwnedAlert requireOwnedAlert(Long alertId) {
        CaishenFundAlertDO alert = caishenFundAlertMapper.selectById(alertId);
        if (alert == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        Long userId = requireUserId();
        CaishenFundWatchDO watch = caishenFundWatchMapper.selectById(alert.getWatchId());
        if (watch == null || !watch.getUserId().equals(userId)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return new OwnedAlert(alert, watch);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    /** 提醒规则及其归属关注 */
    private record OwnedAlert(CaishenFundAlertDO alert, CaishenFundWatchDO watch) {
    }
}