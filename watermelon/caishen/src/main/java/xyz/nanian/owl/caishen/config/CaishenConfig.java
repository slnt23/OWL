package xyz.nanian.owl.caishen.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * caishen 模块配置属性，前缀 {@code caishen}。
 *
 * <p>自注册（@Component + @ConfigurationProperties），默认值写在代码中，
 * 可通过 start 配置或 Nacos 覆盖。示例：</p>
 *
 * <pre>
 * caishen:
 *   schedule:
 *     nav-sync-cron: '0 0 15 * * ?'
 *     daily-summary-cron: '0 30 15 * * ?'
 *     weekly-summary-cron: '0 0 16 ? * MON'
 *   market:
 *     provider: mock
 *   analysis:
 *     provider: mock
 * </pre>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Component
@ConfigurationProperties(prefix = "caishen")
public class CaishenConfig {

    private Schedule schedule = new Schedule();

    private Market market = new Market();

    private Analysis analysis = new Analysis();

    @Data
    public static class Schedule {

        /** 定时任务总开关 */
        private boolean enabled = true;

        /** 净值同步 + 阈值检查 cron，默认每天 15:00 */
        private String navSyncCron = "0 0 15 * * ?";

        /** 日总结 cron，默认每天 15:30 */
        private String dailySummaryCron = "0 30 15 * * ?";

        /** 周总结 cron，默认每周一 16:00 */
        private String weeklySummaryCron = "0 0 16 ? * MON";
    }

    @Data
    public static class Market {

        /** 行情 provider：mock / python（V1 仅 mock） */
        private String provider = "mock";

        private Mock mock = new Mock();
    }

    @Data
    public static class Mock {

        /** 演示基金基线（fundCode 不在列表时 mock 用默认净值 1.0） */
        private List<MockFund> funds = new ArrayList<>(List.of(
                new MockFund("000001", "华夏成长混合", "混合型",
                        new BigDecimal("1.5234"), new BigDecimal("3.8912"), new BigDecimal("1.2345")),
                new MockFund("005827", "易方达蓝筹精选混合", "混合型",
                        new BigDecimal("2.1567"), new BigDecimal("2.1567"), new BigDecimal("-0.5621"))
        ));

        /** 每次同步单位净值浮动步长 */
        private BigDecimal unitNavStep = new BigDecimal("0.008");

        /** 每次同步日收益率浮动步长（%） */
        private BigDecimal dailyReturnRateStep = new BigDecimal("0.30");
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MockFund {

        private String fundCode;

        private String fundName;

        private String fundType;

        private BigDecimal unitNav;

        private BigDecimal accumulatedNav;

        private BigDecimal dailyReturnRate;
    }

    @Data
    public static class Analysis {

        /** 总结 provider：mock / spring-ai / python（V1 默认 mock） */
        private String provider = "mock";
    }
}