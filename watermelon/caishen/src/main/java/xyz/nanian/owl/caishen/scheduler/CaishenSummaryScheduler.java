package xyz.nanian.owl.caishen.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.constant.PeriodType;
import xyz.nanian.owl.caishen.service.SummaryService;

/**
 * 定时生成日/周总结 + 发总结邮件。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "caishen.schedule.enabled", havingValue = "true", matchIfMissing = true)
public class CaishenSummaryScheduler {

    private final SummaryService summaryService;

    @Scheduled(cron = "${caishen.schedule.daily-summary-cron:0 30 15 * * ?}")
    public void generateDailySummaries() {
        log.info("[caishen] 开始生成日总结");
        summaryService.generateForAllUsers(PeriodType.DAILY);
    }

    @Scheduled(cron = "${caishen.schedule.weekly-summary-cron:0 0 16 ? * MON}")
    public void generateWeeklySummaries() {
        log.info("[caishen] 开始生成周总结");
        summaryService.generateForAllUsers(PeriodType.WEEKLY);
    }
}
