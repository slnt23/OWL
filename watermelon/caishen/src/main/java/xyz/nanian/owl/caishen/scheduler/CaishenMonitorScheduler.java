package xyz.nanian.owl.caishen.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.client.FundNavData;
import xyz.nanian.owl.caishen.client.MarketDataClient;
import xyz.nanian.owl.caishen.config.CaishenConfig;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.mapper.CaishenFundNavMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundWatchMapper;
import xyz.nanian.owl.caishen.service.AlertService;

import java.time.LocalDate;
import java.util.List;

/**
 * 定时拉取净值 + 检查阈值 + 发提醒邮件。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "caishen.schedule.enabled", havingValue = "true", matchIfMissing = true)
public class CaishenMonitorScheduler {

    private final CaishenFundWatchMapper caishenFundWatchMapper;
    private final CaishenFundNavMapper caishenFundNavMapper;
    private final MarketDataClient marketDataClient;
    private final AlertService alertService;
    private final CaishenConfig caishenConfig;

    @Scheduled(cron = "${caishen.schedule.nav-sync-cron:0 0 15 * * ?}")
    public void syncMarketDataAndCheckAlerts() {
        LocalDate today = LocalDate.now();
        log.info("[caishen] 开始净值同步与阈值检查");

        List<CaishenFundWatchDO> watches = caishenFundWatchMapper.selectList(null);
        List<String> fundCodes = watches.stream()
                .map(CaishenFundWatchDO::getFundCode)
                .distinct()
                .toList();
        if (fundCodes.isEmpty()) {
            log.info("[caishen] 无关注基金，跳过净值同步");
        } else {
            String source = caishenConfig.getMarket().getProvider();
            int updated = 0;
            try {
                List<FundNavData> navList = marketDataClient.fetchLatestNav(fundCodes);
                for (FundNavData data : navList) {
                    if (data == null || data.getFundCode() == null || data.getUnitNav() == null) {
                        continue;
                    }
                    try {
                        CaishenFundNavDO nav = new CaishenFundNavDO();
                        nav.setFundCode(data.getFundCode());
                        nav.setNavDate(data.getNavDate() == null ? today : data.getNavDate());
                        nav.setUnitNav(data.getUnitNav());
                        nav.setAccumulatedNav(data.getAccumulatedNav());
                        nav.setDailyReturnRate(data.getDailyReturnRate());
                        nav.setSource(source);
                        updated += caishenFundNavMapper.upsertNav(nav);
                    } catch (Exception e) {
                        // 单条净值失败不中断批次
                        log.error("[caishen] 净值 upsert 失败 fundCode={}", data.getFundCode(), e);
                    }
                }
            } catch (Exception e) {
                log.error("[caishen] 拉取行情失败", e);
            }
            log.info("[caishen] 净值同步完成，更新 {} 条", updated);
        }

        alertService.checkAndTrigger(today);
        log.info("[caishen] 阈值检查完成");
    }
}
