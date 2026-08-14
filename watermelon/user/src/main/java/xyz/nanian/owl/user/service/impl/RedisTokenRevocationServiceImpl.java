package xyz.nanian.owl.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.security.TokenRevocationService;

import java.util.concurrent.TimeUnit;

/**
 * Redis-backed token revocation implementation for the user module.
 */
@Service
@RequiredArgsConstructor
public class RedisTokenRevocationServiceImpl implements TokenRevocationService {

    private static final String BLACKLIST_KEY_PREFIX = "auth:blacklist:";
    private static final String TOKEN_VERSION_KEY_PREFIX = "auth:token-version:";

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean isRevoked(String jti) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(BLACKLIST_KEY_PREFIX + jti));
    }

    @Override
    public long getTokenVersion(Long userId) {
        String value = stringRedisTemplate.opsForValue().get(TOKEN_VERSION_KEY_PREFIX + userId);
        return value == null ? 0L : Long.parseLong(value);
    }

    @Override
    public void revoke(String jti, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            return;
        }
        stringRedisTemplate.opsForValue()
                .set(BLACKLIST_KEY_PREFIX + jti, "1", ttlSeconds, TimeUnit.SECONDS);
    }

    @Override
    public void bumpVersion(Long userId) {
        Long version = stringRedisTemplate.opsForValue().increment(TOKEN_VERSION_KEY_PREFIX + userId);
        if (version == null) {
            stringRedisTemplate.opsForValue().set(TOKEN_VERSION_KEY_PREFIX + userId, "1");
        }
    }
}
