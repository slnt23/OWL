package xyz.nanian.owl.utils.redis;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.constant.LoginConstant;

/**
 * 检验邮箱验证码是否过期
 *
 * @author slnt23
 * @since 2026/4/11
 */

@Component
public class CodeCacheUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public CodeCacheUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 检测验证码是否过期，
     * @param email
     * @return
     */
    public Boolean isLocked(String email){
        String lockKey = LoginConstant.VERIFICATION_CODE_PREFIX + email;

        return stringRedisTemplate.hasKey(lockKey);
    }


}
