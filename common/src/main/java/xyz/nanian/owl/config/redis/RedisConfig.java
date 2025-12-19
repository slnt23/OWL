package xyz.nanian.owl.config.redis;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Configuration
public class RedisConfig {

    /**
     * 配置序列化
     * @param redisConnectionFactory 工厂
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

//        key 与 hashKey 使用String
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

//        这里序列化与反序列化redis中的value值 使用Json代替jdk   TODO 这个类中的版本只是初版，后续还要改进
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

//        配置序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 可选的，但推荐的只操作字符串
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
         return new StringRedisTemplate(redisConnectionFactory);
    }
}
