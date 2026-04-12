
# 工程简介

## api 模块：对外接口层（Controller + 入口）

- 作用：唯一暴露对外 API 的模块，处理 HTTP 请求、参数校验、组装响应。
- 典型内容：

  - `controller` 包：`RESTful` 接口（如 UserController）。
  - `dto` 包：请求/响应对象（如 UserRegisterReq、UserResp）。
  - `assembler` 包：实体 ↔ DTO 转换（如 MapStruct）。
  - `config` 包：CORS、拦截器、Security 配置。
  - 启动类：`@SpringBootApplication` 主类。


- 依赖关系：

  - 依赖 `domain`（调用业务）。
  - 依赖 `common`（用工具）。


- 为什么需要？
隔离接口与业务。改接口不影响核心逻辑，支持多端（Web、App）。

## 定义对外暴露的接口契约（Interface Contract），主要用于不同微服务之间的调用。

