package xyz.nanian.owl.config.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.lettuce.core.ReadFrom;
import org.springframework.boot.autoconfigure.data.redis.LettuceClientConfigurationBuilderCustomizer;
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

        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        这里序列化与反序列化redis中的value值 使用Json代替jdk
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(om);

//        以下方法已经废弃
//        value使用json
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
//                new Jackson2JsonRedisSerializer<>(Object.class);
//
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL
//        );
//        jackson2JsonRedisSerializer.setObjectMapper(mapper);


//        配置序列化方式
//        key
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        value
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * 配置序列化
     * 可选的，但推荐的只操作字符串，推荐使用这个，感觉这个和哈希都好
     * @param redisConnectionFactory 连接工厂
     * @return StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
         return new StringRedisTemplate(redisConnectionFactory);
    }

    /**
     * 配置Redis 的读写分离，目前是优先从子节点 读取，主节点 写入
     * @return
     */
    @Bean
    public LettuceClientConfigurationBuilderCustomizer configurationBuilderCustomizer() {
        return clientConfigurationBuilder ->
                clientConfigurationBuilder.readFrom(ReadFrom.REPLICA_PREFERRED);
    }
}
