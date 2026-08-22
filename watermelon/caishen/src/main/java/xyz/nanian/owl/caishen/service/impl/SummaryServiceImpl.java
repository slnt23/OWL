package xyz.nanian.owl.caishen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.caishen.client.AssetAnalysisClient;
import xyz.nanian.owl.caishen.constant.PeriodType;
import xyz.nanian.owl.caishen.constant.PeriodType.Range;
import xyz.nanian.owl.caishen.constant.SummaryStatus;
import xyz.nanian.owl.caishen.domain.dto.SummaryQueryDTO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenSummaryDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenUserEmailDO;
import xyz.nanian.owl.caishen.domain.vo.SummaryVO;
import xyz.nanian.owl.caishen.mapper.CaishenFundMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundNavMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundWatchMapper;
import xyz.nanian.owl.caishen.mapper.CaishenSummaryMapper;
import xyz.nanian.owl.caishen.mapper.CaishenUserEmailMapper;
import xyz.nanian.owl.caishen.mapstruct.CaishenSummaryConvert;
import xyz.nanian.owl.caishen.service.SummaryService;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.mail.MailMessage;
import xyz.nanian.owl.common.mail.MailService;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 总结生成与查询实现。
 *
 * <p>状态机 PENDING → PROCESSING → SUCCESS / FAILED 在 DB 中完整保留；
 * 生成过程不包长事务（AI 调用期间各 DB 步骤独立提交）。</p>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100");
    private static final int MAX_PAGE_SIZE = 100;

    private final CaishenSummaryMapper caishenSummaryMapper;
    private final CaishenFundWatchMapper caishenFundWatchMapper;
    private final CaishenFundNavMapper caishenFundNavMapper;
    private final CaishenFundMapper caishenFundMapper;
    private final CaishenUserEmailMapper caishenUserEmailMapper;
    private final CaishenSummaryConvert caishenSummaryConvert;
    private final AssetAnalysisClient assetAnalysisClient;
    private final MailService mailService;
    private final ObjectMapper objectMapper;

    @Override
    public ResultPage<SummaryVO> pageMy(SummaryQueryDTO query) {
        Long userId = requireUserId();
        int pageNum = query.getPageNum() == null || query.getPageNum() <= 0 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() <= 0
                ? 10 : Math.min(query.getPageSize(), MAX_PAGE_SIZE);

        LambdaQueryWrapper<CaishenSummaryDO> wrapper = Wrappers.<CaishenSummaryDO>lambdaQuery()
                .eq(CaishenSummaryDO::getUserId, userId);
        if (query.getPeriodType() != null && !query.getPeriodType().isBlank()) {
            wrapper.eq(CaishenSummaryDO::getPeriodType, query.getPeriodType().trim());
        }
        wrapper.orderByDesc(CaishenSummaryDO::getCreateTime);

        Page<CaishenSummaryDO> page = new Page<>(pageNum, pageSize);
        IPage<CaishenSummaryDO> result = caishenSummaryMapper.selectPage(page, wrapper);
        List<SummaryVO> records = caishenSummaryConvert.toVOList(result.getRecords());

        ResultPage<SummaryVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public SummaryVO getById(Long id) {
        Long userId = requireUserId();
        CaishenSummaryDO summary = caishenSummaryMapper.selectById(id);
        if (summary == null || !summary.getUserId().equals(userId)) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return caishenSummaryConvert.toVO(summary);
    }

    @Override
    public Long generateForUser(Long userId, PeriodType periodType) {
        Range range = periodType.resolveRange(LocalDate.now());
        List<CaishenFundWatchDO> watches = caishenFundWatchMapper.selectList(
                Wrappers.<CaishenFundWatchDO>lambdaQuery().eq(CaishenFundWatchDO::getUserId, userId));
        if (watches.isEmpty()) {
            throw new BizException(ResultStatus.DATA_NOT_EXIST);
        }

        List<Map<String, Object>> fundMetrics = buildFundMetrics(watches, range);
        String metricSnapshot = toJson(fundMetrics);

        LocalDateTime now = LocalDateTime.now();
        CaishenSummaryDO summary = new CaishenSummaryDO();
        summary.setUserId(userId);
        summary.setPeriodType(periodType.name());
        summary.setStartDate(range.startDate());
        summary.setEndDate(range.endDate());
        summary.setMetricSnapshot(metricSnapshot);
        summary.setStatus(SummaryStatus.PENDING.name());
        summary.setCreateTime(now);
        summary.setUpdateTime(now);
        caishenSummaryMapper.insert(summary);
        Long summaryId = summary.getId();

        try {
            updateStatus(summaryId, SummaryStatus.PROCESSING.name(), null);
            String summaryText = assetAnalysisClient.generateSummary(metricSnapshot, periodType.name());
            updateStatus(summaryId, SummaryStatus.SUCCESS.name(), null);
            sendSummaryEmail(summary, periodType, fundMetrics, summaryText);
            return summaryId;
        } catch (Exception e) {
            updateStatus(summaryId, SummaryStatus.FAILED.name(), truncate(e.getMessage(), 500));
            throw e;
        }
    }

    @Override
    public void generateForAllUsers(PeriodType periodType) {
        List<CaishenFundWatchDO> watches = caishenFundWatchMapper.selectList(null);
        Set<Long> userIds = watches.stream()
                .map(CaishenFundWatchDO::getUserId)
                .collect(Collectors.toSet());
        for (Long userId : userIds) {
            try {
                generateForUser(userId, periodType);
            } catch (Exception e) {
                // 单用户失败不影响其他用户
                log.error("[caishen] 为用户生成总结失败 userId={} periodType={}", userId, periodType, e);
            }
        }
    }

    // ------------------------------------------------------------
    // 私有辅助方法
    // ------------------------------------------------------------

    private List<Map<String, Object>> buildFundMetrics(List<CaishenFundWatchDO> watches, Range range) {
        List<Map<String, Object>> fundList = new ArrayList<>();
        for (CaishenFundWatchDO watch : watches) {
            List<CaishenFundNavDO> navs = caishenFundNavMapper.selectList(
                    Wrappers.<CaishenFundNavDO>lambdaQuery()
                            .eq(CaishenFundNavDO::getFundCode, watch.getFundCode())
                            .between(CaishenFundNavDO::getNavDate, range.startDate(), range.endDate())
                            .orderByAsc(CaishenFundNavDO::getNavDate));
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("code", watch.getFundCode());
            item.put("name", fundName(watch.getFundCode()));
            if (navs.isEmpty()) {
                item.put("periodChange", null);
                item.put("dayChange", null);
            } else {
                item.put("periodChange", periodChangePercent(navs.get(0), navs.get(navs.size() - 1)));
                item.put("dayChange", navs.get(navs.size() - 1).getDailyReturnRate());
            }
            fundList.add(item);
        }
        return fundList;
    }

    private String toJson(List<Map<String, Object>> fundMetrics) {
        Map<String, Object> snapshot = new LinkedHashMap<>();
        snapshot.put("funds", fundMetrics);
        try {
            return objectMapper.writeValueAsString(snapshot);
        } catch (JsonProcessingException e) {
            throw new BizException(ResultStatus.BIZ_ERROR);
        }
    }

    private BigDecimal periodChangePercent(CaishenFundNavDO first, CaishenFundNavDO last) {
        if (first.getUnitNav() == null || last.getUnitNav() == null
                || first.getUnitNav().compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return last.getUnitNav().subtract(first.getUnitNav())
                .multiply(ONE_HUNDRED)
                .divide(first.getUnitNav(), 4, RoundingMode.HALF_UP);
    }

    private void updateStatus(Long summaryId, String status, String errorMessage) {
        LambdaUpdateWrapper<CaishenSummaryDO> wrapper = Wrappers.<CaishenSummaryDO>lambdaUpdate()
                .eq(CaishenSummaryDO::getId, summaryId)
                .set(CaishenSummaryDO::getStatus, status)
                .set(CaishenSummaryDO::getUpdateTime, LocalDateTime.now());
        if (errorMessage != null) {
            wrapper.set(CaishenSummaryDO::getErrorMessage, errorMessage);
        }
        caishenSummaryMapper.update(null, wrapper);
    }

    private void sendSummaryEmail(CaishenSummaryDO summary, PeriodType periodType,
                                  List<Map<String, Object>> fundMetrics, String summaryText) {
        CaishenUserEmailDO userEmail = caishenUserEmailMapper.selectById(summary.getUserId());
        if (userEmail == null || userEmail.getEmail() == null || userEmail.getEmail().isBlank()) {
            log.warn("[caishen] 用户 {} 无邮箱，跳过总结邮件", summary.getUserId());
            return;
        }
        String periodLabel = periodLabel(periodType);
        String subject = "【OWL 理财" + periodLabel + "报】您关注的基金本" + periodLabel + "变化总结";
        String body = buildSummaryBody(summary, fundMetrics, summaryText);
        try {
            mailService.send(MailMessage.builder()
                    .to(userEmail.getEmail())
                    .subject(subject)
                    .body(body)
                    .html(true)
                    .build());
        } catch (Exception e) {
            log.error("[caishen] 总结邮件发送失败 userId={}", summary.getUserId(), e);
        }
    }

    private String buildSummaryBody(CaishenSummaryDO summary,
                                    List<Map<String, Object>> fundMetrics, String summaryText) {
        StringBuilder sb = new StringBuilder();
        sb.append("<p>您关注的 ").append(fundMetrics.size()).append(" 只基金本周期（")
                .append(summary.getStartDate()).append(" ~ ").append(summary.getEndDate())
                .append("）变化如下：</p><ul>");
        for (Map<String, Object> item : fundMetrics) {
            Object change = item.get("periodChange");
            sb.append("<li>").append(item.get("name")).append("（").append(item.get("code"))
                    .append("）：周期涨跌幅 ")
                    .append(change == null ? "-" : change + "%")
                    .append("</li>");
        }
        sb.append("</ul><p>AI 总结：").append(summaryText == null ? "" : summaryText).append("</p>");
        return sb.toString();
    }

    private String periodLabel(PeriodType periodType) {
        return switch (periodType) {
            case DAILY -> "日";
            case WEEKLY -> "周";
            case MONTHLY -> "月";
        };
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

    private String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        return s.length() <= max ? s : s.substring(0, max);
    }
}
