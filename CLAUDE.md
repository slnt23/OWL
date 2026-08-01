# CLAUDE.md

本文件为 Claude Code（claude.ai/code）在此仓库中工作时提供指引。

## 构建与运行

第三层：ADR（架构决策记录）—— 大厂的隐藏武器
定位：记录"为什么这样做"，而不是"怎么做"

```bash
# 全模块编译（跳过测试）
./mvnw clean package -DskipTests

# 单模块测试
./mvnw test -pl watermelon/user

# 启动应用（需 MySQL、Redis、RabbitMQ、Nacos、MinIO）
java -jar start/target/start-0.0.1-SNAPSHOT.jar
```

默认激活的 Spring profile 为 `dev`（参见 `start/src/main/resources/application-dev.yml`）。基础设施连接配置在 dev 中默认注释 —— 取消注释或通过环境变量传入。

## 架构

多模块 Maven 项目：**Spring Boot 3.3.13 + Java 17 + MyBatis-Plus 3.5.5**。

### 模块依赖链

```
common（基础层：Result、异常、拦截器、JWT、MyBatisPlus/Knife4j 配置）
  → infra（中间件适配器：MinIO、Redis、RabbitMQ、Bloom Filter）
    → log（AOP 业务日志 + TraceId）
      → api（接口契约层，Feign 接口 + 对外 API）
        → watermelon/*（业务模块）
            ↑
common + infra + watermelon/* ─── start（启动模块，聚合所有模块）
```

- `common` 是基础层：配置、拦截器、统一 `Result<T>` 响应包装、`GlobalExceptionHandler`、JWT 工具、邮件工具、正则工具。
- `infra` 是中间件适配层：MinIO 文件存储、Redis 缓存、RabbitMQ 消息队列、Guava 布隆过滤器。
- `watermelon/` 包含业务模块：**user**（用户认证中心）、**sugarcane**（价多多-价格追踪）、**crow**（乌鸦-AI 对话）、**pitaya**（火龙果-电商平台）、**administration**（后台管理）。

### 业务模块分层模式

每个 watermelon 子模块遵循统一的分层结构：

```
controller → service（interface + impl）→ mapper（MyBatis-Plus）→ domain/entity
                  ↓
            mapstruct（Entity ↔ DTO/VO 转换，通过 MapStruct）
```

实体类使用 `*DO` 后缀，DTO/VO 遵循 MyBatis-Plus 规范（`@TableName`、`@TableId` 等）。

### 关键横切关注点

- **统一响应**：所有 controller 返回 `Result<T>`（来自 `common`）。使用 `Result.success(data)` / `Result.fail(ResultStatus.xxx)`。
- **认证**：基于 JWT。`LoginInterceptor` 读取 `Authorization: Bearer <token>` 请求头，解析 claims，将用户信息存入 `UserContext`（ThreadLocal）。`afterCompletion` 中清理。路径排除规则见 `WebMvcConfig`。
- **业务日志**：在 service 方法上使用 `@BizLog(module = "用户", action = "更新用户信息")` —— `log` 模块中的 AOP 切面记录到数据库。
- **异常处理**：`GlobalExceptionHandler`（`@RestControllerAdvice`）捕获 `BizException`、参数校验异常、404、405 及通用异常，统一返回 `Result.fail(...)`。

### 需要的基础设施服务

MySQL 8.4（数据库：`pitaya`），Redis 6.2 Sentinel 集群，RabbitMQ 3.12，Nacos（Spring Cloud Alibaba），MinIO（对象存储），DeepSeek API（通过 Spring AI 的 OpenAI 兼容协议接入）。

## 代码规范

- 注解：`@Slf4j`、`@RequiredArgsConstructor`（构造器注入）、`@Service`、`@RestController`。
- MyBatis-Plus：`LambdaUpdateWrapper` 进行安全字段引用；分页 + 乐观锁在 `MybatisPlusConfig` 中配置。
- MapStruct 用于 entity↔DTO 转换（如 `UserConvert`）。
- 多环境：`application-dev.yml`（全注释配置）和 `application-prod.yml`（环境变量占位符）。
- Docker：`Dockerfile` 多阶段构建，仅 JRE 运行时镜像，暴露 8080 端口。

## 编写新的 watermelon 模块

1. 创建 `watermelon/<name>/pom.xml` —— 依赖 `api`（或 `log`）接入依赖链。
2. 将模块加入根 `pom.xml` 的 `<modules>` 块和 `<dependencyManagement>`。
3. 遵循标准分层包结构：`controller`、`service`（+ `impl`）、`mapper`、`domain/{entity,dto,vo}`、`mapstruct`。
4. 在 `common` 的 `WebMvcConfig` 中注册拦截器路径排除规则。
