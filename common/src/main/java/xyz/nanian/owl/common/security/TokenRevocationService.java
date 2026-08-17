package xyz.nanian.owl.common.security;

/**
 * Token 撤销与版本控制接口。
 *
 * <p>JWT 认证过滤器通过该接口校验 token 是否被撤销、tokenVersion 是否仍然有效。
 * 具体实现由持有 Redis 或其他存储的模块提供，例如 user 模块的
 * {@code RedisTokenRevocationServiceImpl}。</p>
 */
public interface TokenRevocationService {

    /**
     * 判断指定 jti 是否已加入黑名单。
     *
     * @param jti token 唯一标识
     * @return true 表示已撤销
     */
    boolean isRevoked(String jti);

    /**
     * 获取指定用户当前的 token 版本号。
     *
     * @param userId 用户主键 ID
     * @return 当前 token 版本号，不存在时返回 0
     */
    long getTokenVersion(Long userId);

    /**
     * 将指定 jti 加入黑名单。
     *
     * @param jti        token 唯一标识
     * @param ttlSeconds 黑名单保留秒数
     */
    void revoke(String jti, long ttlSeconds);

    /**
     * 将指定用户的 token 版本号加一，使旧 token 全部失效。
     *
     * @param userId 用户主键 ID
     */
    void bumpVersion(Long userId);
}
