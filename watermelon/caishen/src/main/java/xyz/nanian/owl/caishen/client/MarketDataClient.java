package xyz.nanian.owl.caishen.client;

import java.util.List;

/**
 * 行情数据获取接口。Java 侧只做调用方，不直接实现行情源；
 * V1 用 {@code MockMarketDataClient}，后续可切换 Python 服务实现。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface MarketDataClient {

    /**
     * 获取指定基金代码列表的最新净值。
     *
     * @param fundCodes 基金代码列表
     * @return 净值数据列表
     */
    List<FundNavData> fetchLatestNav(List<String> fundCodes);
}
