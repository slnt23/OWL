# ADR-006: 个人博客模块（mango）设计

| 属性     | 值          |
| -------- | ----------- |
| 状态     | 提案中      |
| 负责人   | mango Owner |
| 创建时间 | 2026-08-22  |
| 更新时间 | 2026-08-22  |

## 背景

OWL 需要新增个人博客模块。前端模块 `src/modules/blog` 已按真实页面提出后端接口与数据需求，需要确定后端模块的代号、落点、数据模型与接口契约。此前 [ADR-004](ADR-004-个人博客模块设计.md) 以代号 kiwi 提出过一版设计，本文档基于前端需求对其进行合并与修订，并作为该决策的替代。

## 决策

### 1. 代号与模块落点

**选择**：模块代号改为 mango，替代 ADR-004 的 kiwi。模块目录 `watermelon/mango`，Maven artifactId `mango`，基础包 `xyz.nanian.owl.mango`。

**原因**：

- 遵循现有水果命名约定（crow、pitaya、sugarcane）。
- 用户指定代号为 mango，弃用旧代号 kiwi。

**备选**：沿用 `watermelon/blog` + kiwi 代号，已废弃。

### 2. 接口前缀与表命名

**选择**：公开接口前缀 `GET /api/blog/**`，数据表沿用 `blog_` 前缀（`blog_post`、`blog_tag`、`blog_post_tag`、`blog_category`、`blog_profile`、`blog_education`、`blog_skill_category`、`blog_skill_item`）。

**原因**：

- 前端模块路径为 `src/modules/blog`，接口前缀 `/api/blog` 与前端零改动对齐。
- `post` / `tag` / `category` 等通用表名加 `blog_` 前缀避免在 OWL 单库中歧义。
- 代号 mango 仅用于模块内部命名，不进入对外契约。

**备选**：全部使用 mango 命名（表 `mango_*`、接口 `/api/mango`），需前端同步改名，已否决。

### 3. 数据模型合并 ADR-004

**选择**：采用前端设计稿的 8 张表，删除 ADR-004 中的 `comment` 表与 `html_content` 列。

**原因**：

- 前端设计稿 P0-P2（文章、标签、个人信息、教育、技能）未涉及评论，评论延后迭代。
- 第一版返回 Markdown 原文、前端渲染，不缓存渲染 HTML，省去 `html_content` 冗余列。
- 时间字段统一为 `create_time` / `update_time`，与 OWL 数据库规范一致。

**备选**：保留评论与 RSS，范围过大，v1 不采纳。

### 4. 鉴权与可见性

**选择**：`GET /api/blog/**` 公开（SecurityConfig 白名单），写接口要求登录；草稿默认不可见，登录用户传 `includeDraft=true` 可见。

**原因**：

- 前台访客可读，站长后台可写。
- 个人博客写入者单一，第一版不强制 ADMIN 角色，后续可用 `@PreAuthorize` 收紧。

### 5. slug 与 readTime 自动生成

**选择**：slug 不传时由后端按 title 自动生成（英文 kebab-case，中文用拼音或 ID 兜底），保证唯一；readTime 不传时按正文字数估算。

**原因**：

- URL 友好且稳定，前端以 `/blog/posts/{slug}` 拼接链接。
- 减少前端重复计算，字段语义清晰。

## 后果

- 新增 8 张表，依赖 `user` 表（`blog_post.created_by`）。
- 评论、RSS、全文检索、服务端 Markdown 渲染不在 v1 范围，后续单独迭代。
- 前端需将 `constants/` 硬编码数据替换为接口调用，时间字段名对齐为 `publishTime` / `createTime`。

## 待定

- 评论功能（`blog_comment` 表，嵌套回复 + 审核）。
- 服务端 Markdown 渲染（flexmark-java + jsoup）以支持 SEO 与 XSS 收敛。
- 全文检索是否迁移 Elasticsearch。
- 写入权限是否收紧为 ADMIN。

## 相关文档

- [mango 博客模块开发计划](../plans/mango-blog-module-plan.md)（综合方案与实施步骤）
- [ADR-004 个人博客模块设计（已废弃）](ADR-004-个人博客模块设计.md)
- [mango canonical DDL](../database/OWL/mango/mango_db.sql)
