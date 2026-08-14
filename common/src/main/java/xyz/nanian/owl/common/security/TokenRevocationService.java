package xyz.nanian.owl.common.security;

/**
 * Token revocation contract used by the JWT filter.
 * Implementations should be provided by modules that own Redis or another store.
 */
public interface TokenRevocationService {

    boolean isRevoked(String jti);

    long getTokenVersion(Long userId);

    void revoke(String jti, long ttlSeconds);

    void bumpVersion(Long userId);
}
