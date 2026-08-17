package xyz.nanian.owl.user.utils;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.exception.LoginFailureException;
import xyz.nanian.owl.user.constant.LoginConstant;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * 检验邮箱验证码是否过期
 *
 * @author slnt23
 * @since 2026/4/11
 */

@Component
public class CodeCacheUtil {

    private final StringRedisTemplate stringRedisTemplate;

    private static final DefaultRedisScript<Long> VERIFY_AND_CONSUME_SCRIPT = new DefaultRedisScript<>(
            "if redis.call('get', KEYS[1]) == ARGV[1] "
                    + "then redis.call('del', KEYS[1]) return 1 "
                    + "else return 0 end",
            Long.class);

    public CodeCacheUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 判断邮箱当前是否处于验证码冷却期。
     *
     * @param email 邮箱
     * @return true 表示验证码仍有效/冷却中
     */
    public Boolean isLocked(String email){
        String lockKey = LoginConstant.VERIFICATION_CODE_PREFIX + email;

        return stringRedisTemplate.hasKey(lockKey);
    }

    /**
     * 原子校验并消费验证码，成功时立即删除 Redis 中的验证码。
     *
     * @param email 邮箱
     * @param code  用户输入的验证码
     * @return true 表示校验通过且已消费
     */
    public boolean verifyAndConsume(String email, String code) {
        String key = LoginConstant.VERIFICATION_CODE_PREFIX + email;
        Long result = stringRedisTemplate.execute(
                VERIFY_AND_CONSUME_SCRIPT,
                Collections.singletonList(key),
                code);
        return Long.valueOf(1L).equals(result);
    }

    /**
     * 验证码错误次数 +1，首次错误时设置过期时间。
     *
     * @param email 邮箱
     * @return 当前累计错误次数
     */
    public long increaseAttempt(String email) {
        String key = LoginConstant.CODE_ATTEMPT_PREFIX + email;
        Long count = stringRedisTemplate.opsForValue().increment(key);
        if (count != null && count == 1L) {
            stringRedisTemplate.expire(key, LoginConstant.CODE_EXPIRE_MINUTES, TimeUnit.MINUTES);
        }
        return count == null ? 1L : count;
    }

    /**
     * 当前邮箱是否已达到验证码错误次数上限。
     *
     * @param email 邮箱
     * @return true 表示已超限
     */
    public boolean isAttemptExceeded(String email) {
        String key = LoginConstant.CODE_ATTEMPT_PREFIX + email;
        String value = stringRedisTemplate.opsForValue().get(key);
        if (value == null) {
            return false;
        }
        return Long.parseLong(value) >= LoginConstant.CODE_ATTEMPT_LIMIT;
    }

    /**
     * 清理邮箱的验证码和错误次数记录。
     *
     * @param email 邮箱
     */
    public void clear(String email) {
        stringRedisTemplate.delete(LoginConstant.VERIFICATION_CODE_PREFIX + email);
        stringRedisTemplate.delete(LoginConstant.CODE_ATTEMPT_PREFIX + email);
    }

    /**
     * 校验验证码，失败或超限时抛出登录异常，成功时清理错误次数。
     *
     * @param email 邮箱
     * @param code  用户输入的验证码
     */
    public void verifyOrThrow(String email, String code) {
        if (isAttemptExceeded(email)) {
            clear(email);
            throw new LoginFailureException(ResultStatus.CODE_ATTEMPT_EXCEEDED);
        }
        if (!verifyAndConsume(email, code)) {
            long attempts = increaseAttempt(email);
            if (attempts >= LoginConstant.CODE_ATTEMPT_LIMIT) {
                clear(email);
                throw new LoginFailureException(ResultStatus.CODE_ATTEMPT_EXCEEDED);
            }
            throw new LoginFailureException(ResultStatus.VERIFY_CODE_ERROR);
        }
        stringRedisTemplate.delete(LoginConstant.CODE_ATTEMPT_PREFIX + email);
    }

}
