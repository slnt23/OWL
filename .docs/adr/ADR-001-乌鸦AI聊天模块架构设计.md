# ADR-001: crow AI 聊天模块架构设计

## 状态

已采纳（2026-04）

## 背景

项目需要一个 AI 对话模块（代号 crow / 乌鸦），支持用户与 AI 进行多轮对话。需要决定：AI 接入方式、消息持久化策略、流式输出方案、以及如何让 AI 行为可配置。

## 决策

### 1. 使用 Spring AI + OpenAI 兼容协议接入 DeepSeek

**选择**：通过 Spring AI 的 `spring-ai-starter-model-openai` 统一抽象层调用 DeepSeek API（OpenAI 兼容），而不是直接拼 HTTP 请求调 DeepSeek。

**原因**：
- Spring AI 封装了 ChatClient、流式调用（Flux）、Token 统计（Usage），避免手写 HTTP 客户端
- OpenAI 兼容协议是行业事实标准，后续切换模型（如换 OpenAI / 通义千问）只需改配置，不需改代码
- Spring AI 的 `@RefreshScope` 支持 Nacos 动态刷新 system prompt，运维友好

### 2. 消息"先存后聊"模式

**选择**：用户消息先落库（`message` 表），再查历史消息组装 context，最后调 AI。AI 回复也落库。

**原因**：
- 保证消息不丢失，即使 AI 调用失败，用户消息已存
- 历史消息从 DB 取，不受 AI 服务重启影响
- token 消耗可追踪（`prompt_tokens`、`completion_tokens`、`total_token_count`），便于成本核算
- 历史消息数量可通过 `RecentMessageNumberLimit`（默认 10）控制 context 窗口

### 3. 同时支持流式和非流式

**选择**：提供 `/ai/chat`（非流式，返回 `Result<String>`）和 `/ai/chat/stream`（流式，`produces = TEXT_EVENT_STREAM_VALUE`，返回 `Flux<String>`）两个端点。

**原因**：
- 非流式适合 API 调用、自动化场景
- 流式适合 Web/App 前端，用户体验更好（逐字显示）
- 流式使用 Reactor `Flux`，`doOnComplete` 中异步落库 AI 回复，不阻塞响应流

### 4. Skill 系统：AI 行为模板化

**选择**：引入 Skill 概念——每个 Skill 是一个 prompt 模板，支持 `{{key}}` 占位符，模板文件存储在 MinIO（OSS），运行时动态加载。`SkillExecutorService` 负责模板填充 + 调用 AI。

**原因**：
- 将"AI 能做什么"从代码中解耦，运营/产品可以上传新 Skill 模板到 OSS
- 模板支持参数化（如 `{{product_name}}`），同一个 Skill 适配不同场景
- `MinioSkillRegistryService` 本地缓存 Skill 元数据，避免每次调 AI 都访问 OSS
- 架构上为未来的 Agent/工具调用预留了扩展点

### 5. Conversation 管理：UUID 主键 + 软删除 + Token 追踪

**选择**：会话表 `conversation` 使用 UUID 字符串作为主键（`IdType.INPUT`），而非自增 ID。支持软删除（`@TableLogic`），记录累计 token 消耗（`total_tokens`）。

**原因**：
- UUID 避免客户端可枚举会话 ID，安全性更好
- 软删除保留历史数据，用户可恢复误删会话
- 累计 token 消耗便于按用户/会话维度做成本分析

## 后果

- 需要维护 `message` 和 `conversation` 两张表
- Skill 系统依赖 MinIO，本地开发需启动 MinIO 或 mock
- 流式调用对前端有要求（需支持 SSE/EventSource）
- Token 统计依赖 AI 返回的 Usage，部分模型可能不返回
