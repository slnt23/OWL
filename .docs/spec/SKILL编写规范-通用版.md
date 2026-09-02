# SKILL 编写规范

## 1. 概述

SKILL 是 AI Agent 的可复用技能包，本质是一个包含 `SKILL.md` 的文件夹。通过将专业知识、工作流规范和操作指令固化为结构化文档，让 AI Agent 能够按需加载并执行特定领域任务。

SKILL 遵循 **Agent Skills Specification** 开放标准（由 Anthropic 发起，已捐赠至 Linux 基金会 AAIF），被 Claude Code、Cursor、OpenAI Codex CLI、GitHub Copilot、VS Code 等主流 AI 编程工具广泛支持。

### 核心设计原则

- **渐进式披露**：元数据预加载，正文按需加载，控制上下文成本
- **单一职责**：一个 Skill 只做一件事，做好一件事
- **自包含**：Skill 目录内包含完成该任务所需的所有资源
- **可复用**：一次编写，跨项目、跨工具复用

## 2. 目录结构

```
<skill-name>/
├── SKILL.md              # 必须 - 技能主文件（YAML frontmatter + Markdown 正文）
├── scripts/              # 可选 - 可执行脚本（Python、Shell 等）
├── references/           # 可选 - 参考文档（按需加载，不占上下文）
├── assets/               # 可选 - 静态资源（模板、图片、配置等）
└── tests/                # 可选 - 技能测试用例
```

### 最小结构

```
<skill-name>/
└── SKILL.md              # 仅此一个文件即可构成有效 Skill
```

### 存放位置

| 范围   | 路径                              | 说明                       |
| ------ | --------------------------------- | -------------------------- |
| 项目级 | `.<tool>/skills/<skill-name>/`    | 仅当前项目可用，优先级最高 |
| 用户级 | `~/.<tool>/skills/<skill-name>/`  | 所有项目可用               |
| 内置   | 系统内置目录                      | 平台预装，不可修改         |

> 不同工具的存放路径略有差异，常见约定：Claude Code 使用 `.claude/skills/`，Cursor 使用 `.cursor/skills/`，Trae 使用 `.trae/skills/`。具体以工具文档为准。

## 3. SKILL.md 格式规范

### 3.1 基本结构

```markdown
---
name: "<skill-name>"
description: "<简短描述：功能 + 触发条件>"
---

# <技能标题>

<详细指令、使用指南、示例>
```

### 3.2 YAML Frontmatter 字段

#### 必填字段

| 字段          | 类型   | 说明                                                                                                                                |
| ------------- | ------ | ----------------------------------------------------------------------------------------------------------------------------------- |
| `name`        | string | 技能唯一标识符。使用小写字母 + 连字符，如 `code-reviewer`、`api-doc-generator`。**禁止使用空格、下划线、大写字母**。                |
| `description` | string | **最关键字段**。必须同时包含：(1) 技能做什么，(2) 何时触发。控制在 200 字符以内。Agent 启动时预加载此字段，用于判断是否激活该技能。 |

#### 可选字段

| 字段             | 类型     | 说明                                                                 |
| ---------------- | -------- | -------------------------------------------------------------------- |
| `version`        | string   | 语义化版本号，如 `1.0.0`                                             |
| `user-invocable` | boolean  | 是否允许用户直接调用。默认 `true`。设为 `false` 时仅由系统自动触发。 |
| `allowed-tools`  | string[] | 工具白名单，限制该技能可调用的工具列表                               |
| `model`          | string   | 推荐的模型，如 `claude-sonnet-4-20250514`                            |
| `author`         | string   | 作者信息                                                             |
| `tags`           | string[] | 标签列表，用于分类和搜索，如 `["frontend", "vue", "testing"]`        |

### 3.3 Description 编写规范（最重要）

`description` 是 Agent 判断是否激活 Skill 的**唯一依据**。Agent 启动时预加载所有 Skill 的 description，匹配时才加载完整 SKILL.md。

**格式模板**：

```
<功能描述>. Use when <触发条件1> or <触发条件2>.
```

**好的示例**：

```yaml
# ✅ 清晰说明功能 + 明确触发条件
description: "Reviews code for best practices, bugs, and security issues. Use when user asks for code review, before merging PRs, or when analyzing code quality."

# ✅ 具体场景 + 关键词
description: "Generates RESTful API documentation from TypeScript type definitions. Use when user asks to document APIs, generate OpenAPI specs, or create API reference docs."

# ✅ 包含领域关键词
description: "Frontend project architecture guide for module organization. Use when creating new business modules, refactoring code structure, or asking about project conventions."
```

**坏的示例**：

```yaml
# ❌ 没有触发条件
description: "A skill for code review."

# ❌ 太模糊，无法判断何时触发
description: "Helps with development tasks."

# ❌ 只有功能，没有场景
description: "Generates API documentation."

# ❌ 超过 200 字符
description: "This is a very long description that goes on and on about all the wonderful things this skill can do including but not limited to code review, bug detection, security analysis, performance optimization, and many more features that might be useful in various development scenarios..."
```

### 3.4 Markdown 正文规范

正文是 Skill 被激活后加载的完整指令，应遵循以下结构：

```markdown
---
name: "example-skill"
description: "功能描述 + 触发条件"
---

# <技能名称>

## 概述（Overview）

简要说明技能的核心功能和适用场景（2-3 句话）。

## 何时使用（When to Use）

列举明确的触发场景和判断条件。

## 前置条件（Prerequisites）

列出执行该技能所需的前置条件、依赖、权限等。

## 执行步骤（Instructions）

### 步骤 1：<步骤标题>

<详细操作说明>

### 步骤 2：<步骤标题>

<详细操作说明>

## 示例（Examples）

提供具体的输入/输出示例。

## 边界与限制（Constraints）

- 明确不做什么
- 明确适用范围
- 明确已知限制

## 验证规则（Validation）

说明如何验证技能执行结果是否正确。

## 参考资源（References）

引用 `references/` 目录中的文档：

- [架构说明](references/architecture.md)
- [常见模式](references/patterns.md)
```

## 4. 渐进式加载策略

Skill 采用两阶段加载机制，以控制上下文窗口成本：

```
阶段 1：预加载（启动时）
  → 加载所有 SKILL.md 的 frontmatter（name + description）
  → 极低成本，快速匹配

阶段 2：按需加载（触发时）
  → 匹配到 description 后，加载完整 SKILL.md 正文
  → 正文中的 references/ 引用可进一步按需加载
```

### 内容分层建议

| 层级          | 内容                   | 大小建议   |
| ------------- | ---------------------- | ---------- |
| frontmatter   | name + description     | ≤ 200 字符 |
| SKILL.md 正文 | 核心指令、流程、示例   | ≤ 500 行   |
| references/   | 详细参考文档、架构说明 | 按需加载   |
| assets/       | 模板、配置、静态文件   | 无限制     |

**原则：不要把大段文档塞进 SKILL.md 正文。正文放核心指令，细节放 references/。**

## 5. 编写最佳实践

### 5.1 触发条件设计

- **精确匹配**：description 中包含用户可能使用的确切短语和关键词
- **场景覆盖**：覆盖多种触发方式（直接命令、间接请求、上下文推断）
- **避免冲突**：确保不同 Skill 的 description 不会互相误触发

```yaml
# 覆盖多种触发方式
description: "Frontend component generator. Use when creating new components, scaffolding pages, adding UI elements, or when user asks to create a new component or page."
```

### 5.2 指令编写

- **结构化**：使用清晰的标题层级（## → ### → ####）
- **可执行**：每步指令具体、可操作，避免模糊描述
- **有示例**：关键步骤提供代码示例或输入输出示例
- **有边界**：明确说明不做什么，防止 Agent 越界

### 5.3 引用管理

SKILL.md 正文中引用 `references/` 目录下的文件时，使用相对路径：

```markdown
## 参考

详细的架构设计见 [架构说明](references/architecture.md)。
常见开发模式见 [开发模式](references/patterns.md)。
```

### 5.4 脚本约定

`scripts/` 目录下的脚本应：

- 有明确的 shebang 和依赖声明
- 接受标准输入/输出，便于 Agent 调用
- 提供 `--help` 或使用说明

```bash
scripts/
├── validate.py      # Python 验证脚本
├── scaffold.sh      # Shell 脚手架脚本
└── requirements.txt # Python 依赖声明
```

### 5.5 命名规范

| 项目          | 规范          | 示例                                         |
| ------------- | ------------- | -------------------------------------------- |
| Skill 目录名  | 小写 + 连字符 | `code-reviewer`、`api-doc-generator`         |
| SKILL.md name | 与目录名一致  | `name: "code-reviewer"`                      |
| 脚本文件      | 动词 + 名词   | `validate-config.py`、`generate-template.sh` |
| 参考文档      | 名词短语      | `architecture.md`、`api-patterns.md`         |

## 6. 完整示例

### 示例 1：代码审查 Skill

**目录结构**：

```
code-reviewer/
├── SKILL.md
└── references/
    └── common-issues.md
```

**SKILL.md**：

```markdown
---
name: "code-reviewer"
description: "Reviews code for best practices, bugs, and security issues. Use when user asks for code review, before merging PRs, or when analyzing code quality."
version: "1.0.0"
tags: ["code-review", "quality", "best-practices"]
---

# Code Reviewer

对代码进行系统化审查，覆盖最佳实践、常见错误、安全漏洞和性能问题。

## 何时使用

- 用户明确要求代码审查
- PR 合并前的质量检查
- 询问代码质量或最佳实践
- 新人代码辅导场景

## 前置条件

- 代码文件已存在且可读
- 了解项目架构规范（如有，参考项目文档）

## 审查清单

### 1. 架构与设计

- [ ] 代码组织是否符合项目约定的目录结构
- [ ] 是否违反项目边界约束（如分层架构、模块边界）
- [ ] 职责是否单一，是否有过度耦合

### 2. 类型安全

- [ ] 是否避免使用 `any` 或弱类型
- [ ] API 返回类型是否明确定义
- [ ] 函数签名类型是否完整

### 3. 代码规范

- [ ] 命名是否符合项目约定（变量、函数、类、文件）
- [ ] 是否遵循项目代码风格（缩进、引号、分号等）
- [ ] 是否有不必要的复杂逻辑或过度抽象

### 4. 安全与性能

- [ ] 无硬编码密钥、密码或敏感信息
- [ ] 用户输入是否经过校验和转义
- [ ] 是否存在明显的性能瓶颈（N+1 查询、内存泄漏、不必要重渲染等）

### 5. 可维护性

- [ ] 是否有充分的注释或自文档化的命名
- [ ] 错误处理是否完善
- [ ] 是否有对应的测试覆盖

## 审查报告格式

```
## 代码审查报告

### 概览
- 审查文件：<文件路径>
- 严重问题：N 个
- 建议改进：N 个

### 严重问题
1. [分类] <问题描述> → <建议修复>

### 建议改进
1. [分类] <问题描述> → <建议优化>
```

## 参考

常见问题清单见 [common-issues.md](references/common-issues.md)
```

### 示例 2：API 文档生成 Skill

**目录结构**：

```
api-doc-generator/
├── SKILL.md
├── scripts/
│   └── generate-openapi.py
├── assets/
│   └── template.yaml
└── references/
    └── openapi-conventions.md
```

**SKILL.md**：

```markdown
---
name: "api-doc-generator"
description: "Generates RESTful API documentation and OpenAPI specs from TypeScript types. Use when creating API docs, generating OpenAPI specs, or documenting API endpoints."
version: "1.0.0"
tags: ["api", "documentation", "openapi", "typescript"]
---

# API Doc Generator

从 TypeScript 类型定义自动生成 RESTful API 文档和 OpenAPI 3.0 规范。

## 何时使用

- 为新 API 模块生成文档
- 更新已有 API 的 OpenAPI 规范
- 用户要求生成 API 参考文档

## 前置条件

- 项目使用 TypeScript
- API 类型定义在项目的类型目录中
- Python 3.8+ 可用（用于脚本）

## 执行步骤

### 步骤 1：扫描类型定义

扫描目标 API 模块的类型定义文件，提取 DTO/VO 类型定义。

### 步骤 2：生成 OpenAPI Schema

运行脚本生成 OpenAPI 规范：

```bash
python scripts/generate-openapi.py --input <types-dir> --output <output-dir>/api-docs.yaml
```

### 步骤 3：验证输出

- 确认所有端点都有对应的请求/响应类型
- 确认 OpenAPI 规范通过语法验证
- 确认生成的文档与代码一致

## 输出格式

生成的 OpenAPI 文档放在目标模块的 API 文档目录中。

## 参考

OpenAPI 编写规范见 [openapi-conventions.md](references/openapi-conventions.md)
```

## 7. 验证与检查

### 结构验证

- [ ] `SKILL.md` 存在于 Skill 目录根层级
- [ ] YAML frontmatter 包含 `name` 和 `description` 字段
- [ ] `name` 使用小写 + 连字符格式
- [ ] `description` 控制在 200 字符以内
- [ ] 目录名与 `name` 字段一致

### 内容验证

- [ ] description 包含功能说明 + 触发条件
- [ ] 正文有清晰的结构（标题层级合理）
- [ ] 关键步骤有可执行的指令
- [ ] 包含边界说明和限制条件
- [ ] 引用路径正确（`references/`、`scripts/` 等）

### 功能验证

- [ ] 在目标环境中能正确触发（匹配到正确的场景）
- [ ] 不会与其他 Skill 产生触发冲突
- [ ] 执行结果符合预期

## 8. 常见反模式

| 反模式            | 问题                                   | 正确做法                                     |
| ----------------- | -------------------------------------- | -------------------------------------------- |
| 超大 SKILL.md     | 单文件超过 1000 行，吃掉大量上下文     | 拆分为 references/，正文只放核心指令         |
| 模糊的 description | AI 无法判断何时触发                    | 明确列出触发关键词和场景                     |
| 万能 Skill        | 一个 Skill 做太多事情                  | 按单一职责拆分为多个 Skill                   |
| 无边界说明        | Agent 执行时越界                       | 明确列出不做什么、不适用场景                 |
| 缺少示例          | Agent 难以理解预期输出                 | 提供输入/输出示例                            |
| 硬编码路径        | 跨项目无法使用                         | 使用相对路径和参数化配置                     |

## 9. 参考资源

- [Agent Skills Specification](https://agentskills.io/specification) — 官方开放标准
- [anthropics/skills](https://github.com/anthropics/skills) — Anthropic 官方 Skills 仓库（含 skill-creator 参考实现）
- 本规范基于 2025 年 12 月 Anthropic 发布的 Agent Skills 开放标准，综合了 anthropics/skills 官方仓库、agentskills.io 规范文档及社区最佳实践。