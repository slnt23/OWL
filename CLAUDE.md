# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run

第三层：ADR（架构决策记录）—— 大厂的隐藏武器
定位：记录“为什么这样做”，而不是“怎么做”

```bash
# Build all modules (skip tests)
./mvnw clean package -DskipTests

# Run tests for a single module
./mvnw test -pl watermelon/user

# Start the app (requires MySQL, Redis, RabbitMQ, Nacos, MinIO)
java -jar start/target/start-0.0.1-SNAPSHOT.jar
```

Active Spring profile is `dev` by default (see `start/src/main/resources/application-dev.yml`). Infrastructure connection config is commented out in dev — uncomment or pass via env vars.

## Architecture

Multi-module Maven project: **Spring Boot 3.3.13 + Java 17 + MyBatis-Plus 3.5.5**.

### Module dependency chain

```
common (infrastructure: MinIO, Redis, RabbitMQ, JWT, exceptions, interceptors, Result wrapper)
  → domain (shared DTO/VO)
    → log (AOP business logging + TraceId)
      → api (Feign client interfaces for inter-service calls)
        → watermelon/* (business modules)
            ↑
common ─── start (bootstrap, aggregates all modules)
```

- `common` is the base: config, interceptors, unified `Result<T>` response wrapper, `GlobalExceptionHandler`, and infrastructure adapters (MinIO, Redis, RabbitMQ).
- `watermelon/` contains business modules: **user** (auth & user center), **sugarcane** (price tracking), **crow** (AI chat), **pitaya** (e-commerce), **administration** (admin panel).
- `infrastructure/` top-level module is a skeleton (no Java source yet).

### Business module layered pattern

Every watermelon sub-module follows the same layered structure:

```
controller → service (interface + impl) → mapper (MyBatis-Plus) → domain/entity
                  ↓
            mapstruct (Entity ↔ DTO/VO conversion via MapStruct)
```

Entities use `*DO` suffix, DTOs/VOs follow MyBatis-Plus conventions with `@TableName`, `@TableId`, etc.

### Key cross-cutting concerns

- **Unified response**: All controllers return `Result<T>` (from `common`). Use `Result.success(data)` / `Result.fail(ResultStatus.xxx)`.
- **Auth**: JWT-based. `LoginInterceptor` reads `Authorization: Bearer <token>` header, parses claims, stores user in `UserContext` (ThreadLocal). Cleared on `afterCompletion`. Check `WebMvcConfig` for path exclusions.
- **Business logging**: `@BizLog(module = "用户", action = "更新用户信息")` on service methods — AOP in `log` module records to DB.
- **Exception handling**: `GlobalExceptionHandler` (`@RestControllerAdvice`) catches `BizException`, validation errors, 404, 405, and generic exceptions, returning `Result.fail(...)`.

### Infrastructure services needed

MySQL 8.4 (database: `pitaya`), Redis 6.2 Sentinel cluster, RabbitMQ 3.12, Nacos (Spring Cloud Alibaba), MinIO (object storage), and DeepSeek API (OpenAI-compatible via Spring AI).

## Code conventions

- Annotations: `@Slf4j`, `@RequiredArgsConstructor` (constructor injection), `@Service`, `@RestController`.
- MyBatis-Plus: `LambdaUpdateWrapper` for safe column references; pagination + optimistic locking configured in `MybatisPlusConfig`.
- MapStruct for entity↔DTO conversions (e.g., `UserConvert`).
- Multi-environment: `application-dev.yml` (commented-out configs) and `application-prod.yml` (env-var placeholders).
- Docker: multi-stage build in `Dockerfile`, JRE-only runtime image, exposed on port 8080.

## Writing a new watermelon module

1. Create `watermelon/<name>/pom.xml` — depend on `log` (or `api`) for the dependency chain.
2. Add the module to root `pom.xml` `<modules>` block and `<dependencyManagement>`.
3. Follow the standard layered package structure: `controller`, `service` (+ `impl`), `mapper`, `domain/{entity,dto,vo}`, `mapstruct`.
4. Register interceptors/exclusions in `WebMvcConfig` in `common`.
