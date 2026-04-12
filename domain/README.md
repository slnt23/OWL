# 工程简介

## domain 模块：领域核心（业务实体 & 逻辑）

- 作用：存放 核心业务模型和领域服务，是项目的 “心脏”。遵循 DDD 思想，纯业务、无框架依赖。
- 典型内容：

  - `entity` 包：JPA 实体类（如 User、Order），对应数据库表。
  - `repository` 包：JPA 接口（如 UserRepository extends JpaRepository）。
  - `service` 包：领域服务（如 UserDomainService），处理复杂业务逻辑。
  - `vo/dto` 包：领域对象（如 UserVO）。


- 依赖关系：

  - 依赖 `common`（用工具类）。
  - 不依赖 `api`（保持纯净）。


- 为什么需要？
业务逻辑集中，便于测试和重构。比如用户注册的校验、积分计算，都放这里。

## 存放所有与业务数据相关的模型（Model），它是整个项目的“数据模型中心”。
## 公共 domain 只放跨业务的通用类