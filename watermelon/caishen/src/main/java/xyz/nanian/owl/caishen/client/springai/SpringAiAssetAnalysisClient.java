package xyz.nanian.owl.caishen.client.springai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.caishen.client.AssetAnalysisClient;

/**
 * Spring AI 总结实现。复用 crow 模块 {@code AiConfig} 提供的 {@link ChatClient} Bean，
 * 配置走 {@code spring.ai.openai.*}（Nacos ai.yaml，DeepSeek）。仅在
 * {@code caishen.analysis.provider=spring-ai} 时实例化。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Component
@ConditionalOnProperty(name = "caishen.analysis.provider", havingValue = "spring-ai")
@RequiredArgsConstructor
public class SpringAiAssetAnalysisClient implements AssetAnalysisClient {

    private final ChatClient chatClient;

    @Override
    public String generateSummary(String metricSnapshot, String periodType) {
        return chatClient.prompt()
                .system("你是一名基金投资分析助手。请根据指标快照生成简洁、客观的周期总结，"
                        + "对比各基金表现差异，给出下一周期关注要点，不给出具体投资建议。"
                        + "末尾添加免责声明：以上分析仅供参考，不构成投资建议。")
                .user("周期类型：" + periodType + "\n指标快照：" + metricSnapshot)
                .call()
                .content();
    }
}
