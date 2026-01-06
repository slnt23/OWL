package xyz.nanian.owl.utils.redis;


import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类，业务工具，对于业务层直接使用，
 * 对于这些包装有必要吗？直接在代码中使用可以吗？或者创建对应的包装类（工具类）
 *
 * @author slnt23
 * @since 2025/12/10
 */

@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeout, TimeUnit unit){

        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delete(String key){
        return redisTemplate.delete(key);
    }

    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }
}
