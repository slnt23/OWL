# ADR-003: administration 后台管理模块架构设计

## 状态

已采纳（2026-04）

## 背景

需要一个后台管理面板，管理首页展示内容、用户、角色、以及查看业务日志。需要决定：admin 与 user 的关系、首页展示内容的配置化方案。

## 决策

### 1. admin 和 user 分离为独立模块

**选择**：创建 `watermelon/administration` 独立模块，而非把管理功能放在 user 模块内。admin 有自己的 controller、service、mapper、entity，但复用 user 模块的数据表。

**原因**：
- 职责分离：user 面向 C 端消费者，admin 面向运营/管理员，变更节奏不同
- 安全隔离：admin 接口可独立配置拦截器路径（`/admin/**`），方便后续加权限控制
- 可独立部署：如果 admin 流量大，可以拆为独立服务

### 2. 首页内容配置化

**选择**：通过 `FeatureDO`（产品特性展示）和 `SpotlightDO`（首页轮播/焦点项目）两张配置表，让运营人员通过 API 管理首页展示内容，无需改代码。

**原因**：
- 首页内容变更频繁（促销活动、新功能上线），不适合硬编码
- 支持排序（`sort_order`），运营可以控制展示顺序
- admin 提供完整 CRUD，前端可直接对接

### 3. 管理员复用 user 表数据

**选择**：`UserAdminController` 和 `RoleAdminController` 直接操作 user 模块的数据表（通过 Mapper），而非在 admin 模块建独立的用户/角色表。

**原因**：
- 避免数据冗余：管理员和用户是同一套账号体系
- 权限通过角色（role）区分：`DEFAULT_ROLE = 1` 是普通用户，管理员角色由 admin 分配
- 减少跨模块数据同步问题

### 4. 骨架式开发策略

**选择**：部分控制器（`BizLogAdminController`、`LogAdminController`、`RoleAdminController`、`UserAdminController`）当前只有类壳和空方法，功能尚未实现。`FeatureController` 和 `SpotlightController` 已完整实现。

**原因**：
- 首页展示（Feature/Spotlight）是 P0 需求，优先实现
- 用户管理和角色管理功能复杂（搜索、分页、状态变更、权限分配），留骨架便于后续迭代
- 避免"大而全"的过度设计，按需实现

## 后果

- admin 模块当前处于半完成状态，部分功能待开发
- admin 和 user 共享数据表，需要注意 admin 操作不影响 user 端逻辑
- 后续需要为 admin 接口添加权限校验（当前未实现）

## 2026-08-14 更新

- `RoleDO`/`RoleMapper` 上移到 `api`，登录校验与后台角色管理共用同一模型；`PageDTO` 属于通用分页 DTO，继续留在 common。
- 角色管理保留在 admin 模块（`RoleAdminController`/`RoleAdminService`），user 只保留登录时的角色查询。
- `BizLogAdminController`、`LogAdminController`、`RoleAdminController`、`UserAdminController` 均保留并用 `[KEEP]` 注明用途，后续实现。
- 已删除 user 侧角色空壳控制器/DTO/服务，以及 pitaya 旧地址实体/查询/VO 等整文件注释代码。
