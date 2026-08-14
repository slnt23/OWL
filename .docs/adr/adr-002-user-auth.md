# ADR-002: user 用户认证模块架构设计

## 状态

已采纳（2025-11，于 2026-07 重构）

## 背景

用户中心模块（watermelon/user）需要支持注册、登录、身份认证。需要决定：认证方式、密码策略、会话管理方式。

## 决策

### 1. JWT 无状态认证

**选择**：使用 JWT（jjwt 库，HMAC-SHA256 签名），token 有效期 30 天。`JwtAuthenticationFilter` 从 `Authorization: Bearer <token>` 头解析 token，将用户信息注入 `CurrentUserContext`（ThreadLocal），请求结束自动清理。

**原因**：
- 无状态：不需要 Redis 存 session，服务重启不影响已登录用户
- ThreadLocal 隔离：每个请求的用户信息线程安全，`afterCompletion` 清理防止内存泄漏
- 性能：JWT 解析是本地操作，比每次请求查 Redis/DB 快
- 互操作性：JWT 是标准格式，前端/移动端/第三方服务都可使用

**局限**（已知，未来可能改为双 token）：
- 无法主动失效（除非加黑名单）
- token 过期时间固定，无法续期
- 代码中注释预留了 Redis 校验逻辑（`LOGIN_KEY`），为后续双 token 方案做准备

### 2. 邮箱验证码登录 + 密码登录双模式

**选择**：同时支持两种登录方式：
- 邮箱验证码登录：发送 6 位数字验证码到用户邮箱，验证码存 Redis（5 分钟过期），验证通过后签发 JWT。首次登录自动创建账号。
- 密码登录：用户设置密码后可用。新用户默认密码为空，**不能**用密码登录，必须先通过验证码登录后设置密码。

**原因**：
- 降低注册门槛（无需记密码即可体验产品）
- 验证码存 Redis 而非 DB：有过期机制、读写快、自动清理
- 5 分钟内限制重复发送（`CodeCacheUtil.isLocked()`），防止短信/邮件轰炸
- 密码使用 BCrypt 加密（`BCryptPasswordEncoder`），不可逆
- 新用户默认无密码：引导用户主动设置密码，避免默认密码导致的安全风险

### 3. 登录注册合一

**选择**：验证码登录接口同时承担注册功能——若邮箱未注册则自动创建用户。

**原因**：
- 减少注册摩擦，用户只需输入邮箱+验证码即可完成开户
- 默认用户信息由 `UserConstant` 统一管理（默认用户名、角色、头像、状态）
- 业务简化：前端只需一个"登录"按钮，无需区分登录/注册

### 4. 用户上下文 ThreadLocal 管理

**选择**：`CurrentUserContext` 使用 `ThreadLocal<LoginUser>` 存储当前请求的用户信息（userId、userCode、email、roleName），不存完整 UserDO。

**原因**：
- 只存最小必要信息，减少内存占用
- 避免 ThreadLocal 持有 DB 实体导致懒加载异常
- 通过 `LoginInterceptor.afterCompletion()` 保证清理，防止线程池复用导致的数据串扰

## 后果

- JWT 无法主动失效，用户注销需等 token 过期
- 验证码依赖邮件服务（QQ SMTP），邮件到达率受邮件服务商限制
- 注册登录合一的模式下，用户无法"浏览"后再决定注册（已通过不要求登录的页面解决）
- 2026-07 重构：`LoginConstant`、`MailConstant`、`MailUtil`、`CodeCacheUtil` 从 common 下沉到 user 模块

## 2026-08-14 升级记录

- 邮箱验证码登录自动注册落地：统一使用 `/auth/login-email`；旧 `/auth/register`、`/auth/send-verification` 等接口已注释并标记待删除。
- 登录不再依赖前端传入 role，角色从数据库读取并校验启用状态。
- 新增 `/auth/send-code`、`/auth/password/reset`、`/auth/logout`。
- JWT 增加 `jti` 与 `tokenVersion`，登出黑名单、改密/重置/换绑邮箱递增版本，旧 token 可主动失效。
- 收货地址收归 user 模块，新增 `/user/addresses` CRUD 与默认地址逻辑；pitaya 旧地址实体/查询/转换已注释并标记待删除，订单地址快照改为完整收件信息 JSON。
- 邮件发送下沉到 `common` 的 `MailService`，user 的 `MailUtil`/`MailConstant` 保留并标记待删除；后续 caishen 等模块直接复用 `MailService`。
- 待人工清理项统一注释并使用 `[TO_BE_DELETED]` 标记。
