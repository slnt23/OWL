package xyz.nanian.owl.caishen.client.mock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.client.AssetAnalysisClient;

/**
 * 开发期 Mock 总结，返回模板文本，保证无外部 AI 配置时闭环可用。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Component
@ConditionalOnProperty(name = "caishen.analysis.provider", havingValue = "mock", matchIfMissing = true)
public class MockAssetAnalysisClient implements AssetAnalysisClient {

    @Override
    public String generateSummary(String metricSnapshot, String periodType) {
        return "（Mock 总结）周期：" + periodType
                + "。基于当前指标快照，您关注的基金整体表现平稳，可继续持有观察。"
                + "以上分析仅供参考，不构成投资建议。";
    }
}
