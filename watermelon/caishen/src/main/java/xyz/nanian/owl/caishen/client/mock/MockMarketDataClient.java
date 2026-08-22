package xyz.nanian.owl.caishen.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.client.FundNavData;
import xyz.nanian.owl.caishen.client.MarketDataClient;
import xyz.nanian.owl.caishen.config.CaishenConfig;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 开发期 Mock 行情源。按配置返回演示净值：单位净值随机游走、
 * 日收益率随机浮动，便于本地跑通阈值触发闭环。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Component
@ConditionalOnProperty(name = "caishen.market.provider", havingValue = "mock", matchIfMissing = true)
public class MockMarketDataClient implements MarketDataClient {

    private static final BigDecimal DEFAULT_NAV = new BigDecimal("1.0000");

    private final CaishenConfig config;

    /** 基金代码 -> 当前模拟净值（随机游走状态） */
    private final Map<String, BigDecimal> navState = new ConcurrentHashMap<>();

    public MockMarketDataClient(CaishenConfig config) {
        this.config = config;
    }

    @Override
    public List<FundNavData> fetchLatestNav(List<String> fundCodes) {
        if (fundCodes == null || fundCodes.isEmpty()) {
            return List.of();
        }
        return fundCodes.stream().map(this::generate).toList();
    }

    private FundNavData generate(String fundCode) {
        CaishenConfig.Mock mock = config.getMarket().getMock();
        CaishenConfig.MockFund base = findBase(fundCode);

        BigDecimal prevNav = navState.getOrDefault(fundCode, base.getUnitNav());
        BigDecimal newNav = prevNav.add(signedRandom(mock.getUnitNavStep()))
                .max(BigDecimal.ZERO)
                .setScale(4, RoundingMode.HALF_UP);
        navState.put(fundCode, newNav);

        BigDecimal dailyReturn = signedRandom(mock.getDailyReturnRateStep())
                .setScale(4, RoundingMode.HALF_UP);

        return FundNavData.builder()
                .fundCode(fundCode)
                .navDate(LocalDate.now())
                .unitNav(newNav)
                .accumulatedNav(base.getAccumulatedNav())
                .dailyReturnRate(dailyReturn)
                .build();
    }

    private CaishenConfig.MockFund findBase(String fundCode) {
        return config.getMarket().getMock().getFunds().stream()
                .filter(f -> fundCode.equals(f.getFundCode()))
                .findFirst()
                .orElseGet(() -> new CaishenConfig.MockFund(
                        fundCode, null, null, DEFAULT_NAV, DEFAULT_NAV, BigDecimal.ZERO));
    }

    /** 返回 [-step, +step] 内的随机值 */
    private BigDecimal signedRandom(BigDecimal step) {
        if (step == null || step.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }
        double d = ThreadLocalRandom.current().nextDouble(-1, 1) * step.doubleValue();
        return BigDecimal.valueOf(d);
    }
}
