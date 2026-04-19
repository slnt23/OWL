package xyz.nanian.owl.infrastructure.minio.config;


import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.nanian.owl.infrastructure.minio.properties.MinioProperties;

/**
 * minio对象存储服务，后续可以切换阿里云OSS
 *
 * @author slnt23
 * @since 2026/4/19
 */

@Configuration
public class MinioConfig {


    private final MinioProperties properties;

    public MinioConfig(MinioProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(
                        properties.getAccessKey(),
                        properties.getSecretKey()
                )
                .build();
    }
}
