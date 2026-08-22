package xyz.nanian.owl.caishen.client;

/**
 * 资产分析（AI 总结）接口。V1 用 {@code MockAssetAnalysisClient} 或
 * {@code SpringAiAssetAnalysisClient}，后续可切换 Python 服务实现。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface AssetAnalysisClient {

    /**
     * 根据指标快照生成周期总结文本。
     *
     * @param metricSnapshot 指标快照 JSON
     * @param periodType     周期类型（DAILY / WEEKLY / MONTHLY）
     * @return 总结文本
     */
    String generateSummary(String metricSnapshot, String periodType);
}
