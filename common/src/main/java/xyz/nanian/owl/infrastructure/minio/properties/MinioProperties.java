package xyz.nanian.owl.infrastructure.minio.properties;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * minio 配置属性绑定类
 *
 * @author slnt23
 * @since 2026/4/19
 */

@Data
@Component
@ConfigurationProperties(prefix = "storage.minio")
public class MinioProperties {

    /**
     * MinIO 服务地址
     */
    private String endpoint;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * Secret Key
     */
    private String secretKey;

    /**
     * 是否使用 HTTPS
     */
    private boolean secure = false;

    /**
     * 连接超时时间（毫秒）
     */
    private Integer connectTimeout = 10000;

    /**
     * 写入超时时间（毫秒）
     */
    private Integer writeTimeout = 60000;

    /**
     * 读取超时时间（毫秒）
     */
    private Integer readTimeout = 60000;

    /**
     * 支持的多个 Bucket 列表（核心！）
     */
    @NotEmpty(message = "MinIO 配置至少需要一个 Bucket")
    private List<BucketConfig> buckets;

    /**
     * 内部静态类，用于描述每个桶
     */
    @Data
    public static class BucketConfig {
        /**
         * 桶名称
         */
        private String name;

        /**
         * 可选：桶的描述
         */
        private String description;
    }
}
