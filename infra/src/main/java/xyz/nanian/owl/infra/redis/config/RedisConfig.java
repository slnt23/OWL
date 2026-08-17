package xyz.nanian.owl.infra.redis.config;


import io.lettuce.core.ReadFrom;
import org.springframework.boot.data.redis.autoconfigure.LettuceClientConfigurationBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;

/**
 * Redis 配置类。
 *
 * <p>使用 Spring Data Redis 4 提供的 Jackson 3 通用序列化器
 * {@link GenericJacksonJsonRedisSerializer}，替代已弃用的 Jackson 2 版本。</p>
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * 配置 RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        GenericJacksonJsonRedisSerializer serializer = genericJacksonJsonRedisSerializer();

        // key
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * Lettuce 读写分离配置
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer configurationBuilderCustomizer() {
        return clientConfigurationBuilder ->
                clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
    }

    /**
     * Spring Cache 配置（重点修改部分）
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        GenericJacksonJsonRedisSerializer serializer = genericJacksonJsonRedisSerializer();

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .build();
    }

    /**
     * 创建基于 Jackson 3 的通用 Redis JSON 序列化器。
     *
     * <p>开启默认类型信息，反序列化时保留对象真实类型；
     * 日期时间按字符串写入，不使用时间戳。</p>
     *
     * @return GenericJacksonJsonRedisSerializer 实例
     */
    private GenericJacksonJsonRedisSerializer genericJacksonJsonRedisSerializer() {
        return GenericJacksonJsonRedisSerializer.create(builder -> builder
                .typePropertyName("@class")
                .enableDefaultTyping(BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .allowIfSubType((ctx, clazz) -> true)
                        .build())
                .customize(mapperBuilder -> mapperBuilder
                        .disable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .configure(DeserializationFeature.FAIL_ON_MISSING_EXTERNAL_TYPE_ID_PROPERTY, false)));
    }
}

