package xyz.nanian.owl.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.security.TokenRevocationService;

import java.util.concurrent.TimeUnit;

/**
 * 基于 Redis 的 JWT 撤销与版本控制实现。
 * <p>
 * 登出时将 jti 加入黑名单；修改密码/换绑邮箱/重置密码时递增 tokenVersion，
 * 让旧 token 在下一次请求校验时失效。
 */
@Service
@RequiredArgsConstructor
public class RedisTokenRevocationServiceImpl implements TokenRevocationService {

    private static final String BLACKLIST_KEY_PREFIX = "auth:blacklist:";
    private static final String TOKEN_VERSION_KEY_PREFIX = "auth:token-version:";

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 判断指定 jti 是否已加入黑名单。
     *
     * @param jti token 唯一标识
     * @return true 表示已撤销
     */
    @Override
    public boolean isRevoked(String jti) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jti));
    }

    /**
     * 获取用户当前的 token 版本号，不存在时返回 0。
     *
     * @param userId 用户 ID
     * @return 当前 token 版本
     */
    @Override
    public long getTokenVersion(Long userId) {
        String value = stringRedisTemplate.opsForValue().get(TOKEN_VERSION_KEY_PREFIX + userId);
        return value == null ? 0L : Long.parseLong(value);
    }

    /**
     * 将指定 jti 加入黑名单，有效期与 token 剩余寿命一致。
     *
     * @param jti        token 唯一标识
     * @param ttlSeconds 黑名单保留秒数
     */
    @Override
    public void revoke(String jti, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            return;
        }
        stringRedisTemplate.opsForValue()
                .set(BLACKLIST_KEY_PREFIX + jti, "1", ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * 用户 token 版本 +1，使该用户此前签发的所有 token 失效。
     *
     * @param userId 用户 ID
     */
    @Override
    public void bumpVersion(Long userId) {
        Long version = stringRedisTemplate.opsForValue().increment(TOKEN_VERSION_KEY_PREFIX + userId);
        if (version == null) {
            stringRedisTemplate.opsForValue().set(TOKEN_VERSION_KEY_PREFIX + userId, "1");
        }
    }
}
