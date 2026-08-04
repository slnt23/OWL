# CLAUDE.md

本文件为 Claude Code 在此仓库中工作时提供指引。

## 项目简介

OWL（猫头鹰）是一个基于 Spring Boot 3.3.13 + Java 17 的多模块后端项目，包含用户中心、AI 对话、价格追踪、电商平台和后台管理五个业务方向。项目通过 common（基础能力）、infra（中间件适配）、log（业务日志）等模块沉淀通用能力，业务模块统一放在 watermelon 下，由 start 模块聚合启动。

## 常用命令

```bash
# 全模块编译（跳过测试）
mvn clean package -DskipTests

# 单模块测试
mvn test -pl watermelon/user

# 启动应用（需 MySQL、Redis、RabbitMQ、Nacos、MinIO）
java -jar start/target/start-0.0.1-SNAPSHOT.jar
```

默认激活的 Spring profile 为 `dev`。基础设施连接配置通过 Nacos 导入，本地开发时可在 `start/src/main/resources/application-dev.yml` 取消注释或通过环境变量传入。

## 代码约定

- Controller 统一返回 `common` 模块的 `Result<T>`，使用 `Result.success(...)` / `Result.fail(...)`。
- 登录鉴权基于 JWT + Spring Security：`JwtAuthenticationFilter` 解析 `Authorization: Bearer <token>`，用户信息写入 `SecurityContext`，并通过 `CurrentUserContext`（ThreadLocal）供业务代码使用。
- 需要记录操作日志的 Service 方法使用 `@BizLog(module = "...", action = "...")`，由 `log` 模块的 AOP 切面处理。
- 实体类使用 `*DO` 后缀，DTO/VO 放在对应模块的 `domain` 包，Entity ↔ DTO/VO 转换使用 MapStruct。
- MyBatis-Plus 使用 `LambdaQueryWrapper` / `LambdaUpdateWrapper`，分页与乐观锁配置在 `MybatisPlusConfig`。
- 新增业务模块时创建 `watermelon/<name>`，依赖 `api`（或 `log`）接入依赖链，并在根 `pom.xml` 与 `start` 模块中注册。
