package xyz.nanian.owl.crow.config;


import io.minio.MinioClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.nanian.owl.crow.constant.ChatClientProperties;
import xyz.nanian.owl.crow.service.SkillRegistryService;
import xyz.nanian.owl.crow.service.impl.MinioSkillRegistryService;

/**
 * 有关AI的
 *
 * @author slnt23
 * @since 2026/4/12
 */

@Configuration
public class AiConfig {

    @Bean
    @RefreshScope
    public ChatClient chatClient(ChatClient.Builder builder, ChatClientProperties properties) {
        return builder
                .defaultSystem(properties.getDefaultSystem())
//                .httpClient(okHttpClient())
                .build();
    }

    @Bean
    public SkillRegistryService skillRegistry(MinioClient minioClient) {
        return new MinioSkillRegistryService(minioClient);
    }

//    @Bean
//    public okhttp3.OkHttpClient okHttpClient() {
//        return new okhttp3.OkHttpClient.Builder()
//                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
//                .readTimeout(360, java.util.concurrent.TimeUnit.SECONDS)
//                .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
//                .build();
//    }
}