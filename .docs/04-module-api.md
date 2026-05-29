# api 模块：对外接口层

## 定位

唯一暴露对外 API 的模块，作为各微服务之间的**接口契约层**。当前为骨架模块，实际接口分散在各业务模块中。

## 规划结构

```
xyz.nanian.owl
├── feign/      ← Feign 远程调用接口（规划中）
└── others/     ← 其他共享接口定义（规划中）
```

## 设计意图

| 包 | 用途 |
|----|------|
| `controller` | RESTful 接口定义 |
| `dto` | 请求/响应对象 |
| `assembler` | 实体 ↔ DTO 转换（MapStruct） |
| `config` | CORS、拦截器、Security 配置 |
| `feign` | 微服务间 Feign 调用接口 |

## 依赖关系

- 依赖 `domain`（调用业务模型）
- 依赖 `log`（日志记录）
- 依赖 `common`（工具类）
- 被所有 `watermelon/*` 业务模块依赖

## 设计理由

隔离接口与业务。改接口不影响核心逻辑，支持多端（Web、App）。
