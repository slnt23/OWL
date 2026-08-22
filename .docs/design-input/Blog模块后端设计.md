# Blog 模块后端设计

| 属性     | 值                 |
| -------- | ------------------ |
| 状态     | 规划中             |
| 创建时间 | 2026-08-22         |
| 前端模块 | `src/modules/blog` |
| API 前缀 | `/api/blog`        |

---

## 1. 模块分析

### 1.1 前端现状

Blog 模块当前所有数据均为前端硬编码常量（`constants/posts.ts`、`constants/education.ts`、`constants/skills.ts`），未接入任何后端接口。页面结构如下：

| 页面                     | 组件             | 数据来源                                     | 当前状态 |
| ------------------------ | ---------------- | -------------------------------------------- | -------- |
| BlogPage（个人主页）     | AboutSection     | 硬编码头像/姓名/简介/位置                    | 静态     |
|                          | PostsSection     | `BLOG_POST_PREVIEWS` 常量                    | 静态     |
|                          | EducationSection | `EDUCATIONS` 常量                            | 静态     |
|                          | SkillsSection    | `SKILLS` 常量                                | 静态     |
| BlogListPage（文章列表） | BlogList         | `BLOG_POSTS` 常量 + `useBlogList` composable | 静态     |

### 1.2 前端类型定义

```typescript
// 当前前端定义的类型
interface BlogPost {
  id: number;
  title: string;
  excerpt: string;
  date: string;
  datetime: string;
  tags: string[];
  readTime: string;
  lang: string;
  href: string;
}

interface BlogPostPreview {
  id: number;
  title: string;
  date: string;
  datetime: string;
}

interface EducationItem {
  school: string;
  degree: string;
  period: string;
}

interface SkillCategory {
  category: string;
  items: string[];
}
```

### 1.3 需要后端化的数据

| 数据域                  | 说明                                     | 优先级 |
| ----------------------- | ---------------------------------------- | ------ |
| 博客文章（CRUD + 分页） | 核心功能，文章的增删改查、分页、标签筛选 | P0     |
| 标签体系                | 文章标签的管理与聚合查询                 | P0     |
| 个人信息（About）       | 头像、姓名、简介、位置、社交链接         | P1     |
| 教育经历                | 学历信息的 CRUD                          | P2     |
| 技能标签                | 技能分类与条目的 CRUD                    | P2     |

---

## 2. 数据库设计

### 2.1 ER 关系概览

```
blog_post ──1:N──> blog_post_tag ──N:1──> blog_tag
blog_post ──N:1──> blog_category（可选，文章分类）
blog_profile（单行，站长信息）
blog_education（多条，教育经历）
blog_skill_category ──1:N──> blog_skill_item
```

### 2.2 表结构

#### `blog_post` — 博客文章

| 字段         | 类型         | 约束                        | 说明                                |
| ------------ | ------------ | --------------------------- | ----------------------------------- |
| id           | BIGINT       | PK, AUTO_INCREMENT          | 文章 ID                             |
| title        | VARCHAR(200) | NOT NULL                    | 标题                                |
| excerpt      | VARCHAR(500) | NULL                        | 摘要/简介                           |
| content      | LONGTEXT     | NULL                        | 正文（Markdown）                    |
| cover_image  | VARCHAR(500) | NULL                        | 封面图 URL                          |
| slug         | VARCHAR(200) | UNIQUE                      | URL 友好标识，如 `xv6-os-lab-part8` |
| category_id  | BIGINT       | FK → blog_category.id, NULL | 所属分类                            |
| lang         | VARCHAR(10)  | DEFAULT 'zh'                | 语言标识：zh / en                   |
| read_time    | INT          | NULL                        | 预估阅读时长（分钟）                |
| is_published | TINYINT(1)   | DEFAULT 0                   | 0=草稿, 1=已发布                    |
| is_top       | TINYINT(1)   | DEFAULT 0                   | 是否置顶                            |
| view_count   | INT          | DEFAULT 0                   | 浏览次数                            |
| like_count   | INT          | DEFAULT 0                   | 点赞数                              |
| published_at | DATETIME     | NULL                        | 发布时间（前端展示用）              |
| created_at   | DATETIME     | NOT NULL                    | 创建时间                            |
| updated_at   | DATETIME     | NOT NULL                    | 更新时间                            |
| created_by   | BIGINT       | FK → user.id                | 作者用户 ID                         |

索引：`idx_published_at`、`idx_category_id`、`idx_is_published`、`idx_slug`

#### `blog_category` — 文章分类

| 字段       | 类型        | 约束               | 说明     |
| ---------- | ----------- | ------------------ | -------- |
| id         | BIGINT      | PK, AUTO_INCREMENT | 分类 ID  |
| name       | VARCHAR(50) | NOT NULL, UNIQUE   | 分类名称 |
| slug       | VARCHAR(50) | UNIQUE             | URL 标识 |
| sort_order | INT         | DEFAULT 0          | 排序权重 |
| created_at | DATETIME    | NOT NULL           | 创建时间 |

#### `blog_tag` — 标签

| 字段       | 类型        | 约束               | 说明     |
| ---------- | ----------- | ------------------ | -------- |
| id         | BIGINT      | PK, AUTO_INCREMENT | 标签 ID  |
| name       | VARCHAR(50) | NOT NULL, UNIQUE   | 标签名称 |
| slug       | VARCHAR(50) | UNIQUE             | URL 标识 |
| created_at | DATETIME    | NOT NULL           | 创建时间 |

#### `blog_post_tag` — 文章-标签关联

| 字段    | 类型   | 约束                                 | 说明    |
| ------- | ------ | ------------------------------------ | ------- |
| post_id | BIGINT | FK → blog_post.id, ON DELETE CASCADE | 文章 ID |
| tag_id  | BIGINT | FK → blog_tag.id, ON DELETE CASCADE  | 标签 ID |

PK: `(post_id, tag_id)`

#### `blog_profile` — 站长个人信息（单行配置表）

| 字段         | 类型         | 约束          | 说明                                |
| ------------ | ------------ | ------------- | ----------------------------------- |
| id           | BIGINT       | PK, DEFAULT 1 | 固定单行                            |
| avatar_url   | VARCHAR(500) | NULL          | 头像 URL                            |
| name         | VARCHAR(50)  | NOT NULL      | 显示名称                            |
| tagline      | VARCHAR(200) | NULL          | 一行标签，如 "Developer / Designer" |
| bio          | TEXT         | NULL          | 个人简介（支持 Markdown）           |
| location     | VARCHAR(100) | NULL          | 所在地                              |
| github_url   | VARCHAR(200) | NULL          | GitHub 链接                         |
| website_url  | VARCHAR(200) | NULL          | 个人网站                            |
| email        | VARCHAR(100) | NULL          | 联系邮箱                            |
| codetime_uid | VARCHAR(50)  | NULL          | CodeTime UID（用于徽章）            |
| updated_at   | DATETIME     | NOT NULL      | 更新时间                            |

#### `blog_education` — 教育经历

| 字段       | 类型         | 约束               | 说明      |
| ---------- | ------------ | ------------------ | --------- |
| id         | BIGINT       | PK, AUTO_INCREMENT | ID        |
| school     | VARCHAR(100) | NOT NULL           | 学校名称  |
| degree     | VARCHAR(200) | NOT NULL           | 学位/专业 |
| period     | VARCHAR(100) | NOT NULL           | 时间段    |
| sort_order | INT          | DEFAULT 0          | 排序权重  |
| created_at | DATETIME     | NOT NULL           | 创建时间  |
| updated_at | DATETIME     | NOT NULL           | 更新时间  |

#### `blog_skill_category` — 技能分类

| 字段       | 类型        | 约束               | 说明     |
| ---------- | ----------- | ------------------ | -------- |
| id         | BIGINT      | PK, AUTO_INCREMENT | ID       |
| category   | VARCHAR(50) | NOT NULL           | 分类名称 |
| sort_order | INT         | DEFAULT 0          | 排序权重 |

#### `blog_skill_item` — 技能条目

| 字段        | 类型        | 约束                        | 说明       |
| ----------- | ----------- | --------------------------- | ---------- |
| id          | BIGINT      | PK, AUTO_INCREMENT          | ID         |
| category_id | BIGINT      | FK → blog_skill_category.id | 所属分类   |
| name        | VARCHAR(50) | NOT NULL                    | 技能名称   |
| sort_order  | INT         | DEFAULT 0                   | 分类内排序 |

---

## 3. 接口设计

遵循项目统一规范：`Result<T>` 统一响应、`PageResult<T>` 分页响应、`/api` 前缀、Bearer Token 鉴权。

### 3.1 接口总览

#### 文章相关（公开读取 + 登录管理）

| 方法   | 路径                          | 鉴权 | 请求            | 响应 data            | 说明             |
| ------ | ----------------------------- | ---- | --------------- | -------------------- | ---------------- |
| GET    | `/api/blog/posts`             | 公开 | Query           | `PageResult<PostVO>` | 文章分页列表     |
| GET    | `/api/blog/posts/{id}`        | 公开 | 路径 id         | `PostDetailVO`       | 文章详情         |
| GET    | `/api/blog/posts/slug/{slug}` | 公开 | 路径 slug       | `PostDetailVO`       | 按 slug 获取文章 |
| POST   | `/api/blog/posts`             | 登录 | `PostCreateDTO` | Long                 | 创建文章         |
| PUT    | `/api/blog/posts/{id}`        | 登录 | `PostUpdateDTO` | null                 | 更新文章         |
| DELETE | `/api/blog/posts/{id}`        | 登录 | 路径 id         | null                 | 删除文章         |

#### 分类相关

| 方法   | 路径                        | 鉴权 | 请求          | 响应 data      | 说明     |
| ------ | --------------------------- | ---- | ------------- | -------------- | -------- |
| GET    | `/api/blog/categories`      | 公开 | -             | `CategoryVO[]` | 全部分类 |
| POST   | `/api/blog/categories`      | 登录 | `CategoryDTO` | Long           | 创建分类 |
| PUT    | `/api/blog/categories/{id}` | 登录 | `CategoryDTO` | null           | 更新分类 |
| DELETE | `/api/blog/categories/{id}` | 登录 | 路径 id       | null           | 删除分类 |

#### 标签相关

| 方法   | 路径                  | 鉴权 | 请求     | 响应 data | 说明     |
| ------ | --------------------- | ---- | -------- | --------- | -------- |
| GET    | `/api/blog/tags`      | 公开 | -        | `TagVO[]` | 全部标签 |
| POST   | `/api/blog/tags`      | 登录 | `TagDTO` | Long      | 创建标签 |
| DELETE | `/api/blog/tags/{id}` | 登录 | 路径 id  | null      | 删除标签 |

#### 个人信息

| 方法 | 路径                | 鉴权 | 请求               | 响应 data   | 说明         |
| ---- | ------------------- | ---- | ------------------ | ----------- | ------------ |
| GET  | `/api/blog/profile` | 公开 | -                  | `ProfileVO` | 获取站长信息 |
| PUT  | `/api/blog/profile` | 登录 | `ProfileUpdateDTO` | null        | 更新站长信息 |

#### 教育经历

| 方法   | 路径                       | 鉴权 | 请求           | 响应 data       | 说明         |
| ------ | -------------------------- | ---- | -------------- | --------------- | ------------ |
| GET    | `/api/blog/education`      | 公开 | -              | `EducationVO[]` | 全部教育经历 |
| POST   | `/api/blog/education`      | 登录 | `EducationDTO` | Long            | 新增教育经历 |
| PUT    | `/api/blog/education/{id}` | 登录 | `EducationDTO` | null            | 更新教育经历 |
| DELETE | `/api/blog/education/{id}` | 登录 | 路径 id        | null            | 删除教育经历 |

#### 技能

| 方法   | 路径                               | 鉴权 | 请求               | 响应 data           | 说明                         |
| ------ | ---------------------------------- | ---- | ------------------ | ------------------- | ---------------------------- |
| GET    | `/api/blog/skills`                 | 公开 | -                  | `SkillCategoryVO[]` | 全部技能（含条目）           |
| POST   | `/api/blog/skills/categories`      | 登录 | `SkillCategoryDTO` | Long                | 新增技能分类                 |
| PUT    | `/api/blog/skills/categories/{id}` | 登录 | `SkillCategoryDTO` | null                | 更新技能分类                 |
| DELETE | `/api/blog/skills/categories/{id}` | 登录 | 路径 id            | null                | 删除技能分类（级联删除条目） |
| POST   | `/api/blog/skills/items`           | 登录 | `SkillItemDTO`     | Long                | 新增技能条目                 |
| PUT    | `/api/blog/skills/items/{id}`      | 登录 | `SkillItemDTO`     | null                | 更新技能条目                 |
| DELETE | `/api/blog/skills/items/{id}`      | 登录 | 路径 id            | null                | 删除技能条目                 |

### 3.2 请求模型（DTO）

#### `PostListQuery`（GET 查询参数）

| 字段       | 类型    | 必填 | 说明                |
| ---------- | ------- | ---- | ------------------- |
| pageNum    | integer | 否   | 页码，默认 1        |
| pageSize   | integer | 否   | 每页条数，默认 8    |
| tagId      | integer | 否   | 按标签筛选          |
| categoryId | integer | 否   | 按分类筛选          |
| keyword    | string  | 否   | 标题/摘要关键词搜索 |
| lang       | string  | 否   | 按语言筛选：zh / en |

#### `PostCreateDTO`

| 字段        | 类型      | 必填 | 说明                     |
| ----------- | --------- | ---- | ------------------------ |
| title       | string    | 是   | 标题，最大 200           |
| excerpt     | string    | 否   | 摘要，最大 500           |
| content     | string    | 否   | Markdown 正文            |
| coverImage  | string    | 否   | 封面图 URL               |
| slug        | string    | 否   | URL 标识，不传则自动生成 |
| categoryId  | integer   | 否   | 分类 ID                  |
| lang        | string    | 否   | 默认 zh                  |
| tagIds      | integer[] | 否   | 关联标签 ID 列表         |
| isPublished | boolean   | 否   | 默认 false（草稿）       |
| isTop       | boolean   | 否   | 默认 false               |
| publishedAt | string    | 否   | 发布时间，ISO 8601       |

#### `PostUpdateDTO`

同 `PostCreateDTO`，所有字段可选，仅传需要更新的字段。

#### `CategoryDTO`

| 字段      | 类型    | 必填 | 说明              |
| --------- | ------- | ---- | ----------------- |
| name      | string  | 是   | 分类名称，最大 50 |
| slug      | string  | 否   | URL 标识          |
| sortOrder | integer | 否   | 排序权重          |

#### `TagDTO`

| 字段 | 类型   | 必填 | 说明              |
| ---- | ------ | ---- | ----------------- |
| name | string | 是   | 标签名称，最大 50 |
| slug | string | 否   | URL 标识          |

#### `ProfileUpdateDTO`

| 字段        | 类型   | 必填 | 说明         |
| ----------- | ------ | ---- | ------------ |
| avatarUrl   | string | 否   | 头像 URL     |
| name        | string | 否   | 显示名称     |
| tagline     | string | 否   | 一行标签     |
| bio         | string | 否   | 个人简介     |
| location    | string | 否   | 所在地       |
| githubUrl   | string | 否   | GitHub 链接  |
| websiteUrl  | string | 否   | 个人网站     |
| email       | string | 否   | 联系邮箱     |
| codetimeUid | string | 否   | CodeTime UID |

#### `EducationDTO`

| 字段      | 类型    | 必填 | 说明      |
| --------- | ------- | ---- | --------- |
| school    | string  | 是   | 学校名称  |
| degree    | string  | 是   | 学位/专业 |
| period    | string  | 是   | 时间段    |
| sortOrder | integer | 否   | 排序权重  |

#### `SkillCategoryDTO`

| 字段      | 类型    | 必填 | 说明     |
| --------- | ------- | ---- | -------- |
| category  | string  | 是   | 分类名称 |
| sortOrder | integer | 否   | 排序权重 |

#### `SkillItemDTO`

| 字段       | 类型    | 必填 | 说明        |
| ---------- | ------- | ---- | ----------- |
| categoryId | integer | 是   | 所属分类 ID |
| name       | string  | 是   | 技能名称    |
| sortOrder  | integer | 否   | 分类内排序  |

### 3.3 响应模型（VO）

#### `PostVO`（列表项）

| 字段         | 类型      | 说明             |
| ------------ | --------- | ---------------- |
| id           | Long      | 文章 ID          |
| title        | string    | 标题             |
| excerpt      | string    | 摘要             |
| slug         | string    | URL 标识         |
| coverImage   | string    | 封面图           |
| categoryName | string    | 分类名称         |
| tags         | `TagVO[]` | 标签列表         |
| lang         | string    | 语言             |
| readTime     | integer   | 阅读时长（分钟） |
| isPublished  | boolean   | 是否已发布       |
| isTop        | boolean   | 是否置顶         |
| viewCount    | integer   | 浏览次数         |
| likeCount    | integer   | 点赞数           |
| publishedAt  | string    | 发布时间         |
| createdAt    | string    | 创建时间         |

#### `PostDetailVO`（详情，含正文）

同 `PostVO`，额外包含：

| 字段    | 类型   | 说明          |
| ------- | ------ | ------------- |
| content | string | Markdown 正文 |

#### `CategoryVO`

| 字段      | 类型    | 说明           |
| --------- | ------- | -------------- |
| id        | Long    | 分类 ID        |
| name      | string  | 名称           |
| slug      | string  | URL 标识       |
| postCount | integer | 该分类下文章数 |
| sortOrder | integer | 排序权重       |

#### `TagVO`

| 字段      | 类型    | 说明           |
| --------- | ------- | -------------- |
| id        | Long    | 标签 ID        |
| name      | string  | 名称           |
| slug      | string  | URL 标识       |
| postCount | integer | 该标签下文章数 |

#### `ProfileVO`

| 字段        | 类型   | 说明         |
| ----------- | ------ | ------------ |
| avatarUrl   | string | 头像 URL     |
| name        | string | 显示名称     |
| tagline     | string | 一行标签     |
| bio         | string | 个人简介     |
| location    | string | 所在地       |
| githubUrl   | string | GitHub 链接  |
| websiteUrl  | string | 个人网站     |
| email       | string | 联系邮箱     |
| codetimeUid | string | CodeTime UID |

#### `EducationVO`

| 字段      | 类型    | 说明      |
| --------- | ------- | --------- |
| id        | Long    | ID        |
| school    | string  | 学校名称  |
| degree    | string  | 学位/专业 |
| period    | string  | 时间段    |
| sortOrder | integer | 排序权重  |

#### `SkillCategoryVO`

| 字段      | 类型            | 说明         |
| --------- | --------------- | ------------ |
| id        | Long            | 分类 ID      |
| category  | string          | 分类名称     |
| items     | `SkillItemVO[]` | 技能条目列表 |
| sortOrder | integer         | 排序权重     |

#### `SkillItemVO`

| 字段      | 类型    | 说明     |
| --------- | ------- | -------- |
| id        | Long    | 条目 ID  |
| name      | string  | 技能名称 |
| sortOrder | integer | 排序权重 |

---

## 4. 前端类型映射

后端接口上线后，前端类型需从当前硬编码类型迁移到接口类型：

| 前端当前类型             | 后端 VO                        | 映射说明                    |
| ------------------------ | ------------------------------ | --------------------------- |
| `BlogPost.id`            | `PostVO.id`                    | 直接映射                    |
| `BlogPost.title`         | `PostVO.title`                 | 直接映射                    |
| `BlogPost.excerpt`       | `PostVO.excerpt`               | 直接映射                    |
| `BlogPost.date`          | `PostVO.publishedAt`           | 格式化为日期字符串          |
| `BlogPost.datetime`      | `PostVO.publishedAt`           | 原始 ISO 值                 |
| `BlogPost.tags`          | `PostVO.tags[].name`           | 从 TagVO 数组提取 name      |
| `BlogPost.readTime`      | `PostVO.readTime`              | 拼接为 `"{n} min read"`     |
| `BlogPost.lang`          | `PostVO.lang`                  | 直接映射                    |
| `BlogPost.href`          | `PostVO.slug`                  | 拼接为 `/blog/posts/{slug}` |
| `BlogPostPreview.*`      | `PostVO` 子集                  | 仅取 id/title/publishedAt   |
| `EducationItem.*`        | `EducationVO.*`                | 字段完全对应                |
| `SkillCategory.category` | `SkillCategoryVO.category`     | 直接映射                    |
| `SkillCategory.items`    | `SkillCategoryVO.items[].name` | 从 SkillItemVO 提取 name    |

---

## 5. 实施计划

### Phase 1 — 文章核心（P0）

**目标**：文章 CRUD + 分页 + 标签筛选上线，前端 BlogListPage 接入真实接口。

| 步骤 | 任务                                                                 | 产出                          |
| ---- | -------------------------------------------------------------------- | ----------------------------- |
| 1.1  | 后端建表 `blog_post`、`blog_tag`、`blog_post_tag`、`blog_category`   | DDL + 初始化 SQL              |
| 1.2  | 后端实现文章 CRUD 接口（6 个）                                       | Controller + Service + Mapper |
| 1.3  | 后端实现分类 CRUD 接口（4 个）                                       | Controller + Service + Mapper |
| 1.4  | 后端实现标签 CRUD 接口（3 个）                                       | Controller + Service + Mapper |
| 1.5  | 前端 `src/modules/blog/api/` 新建 `post.ts`、`category.ts`、`tag.ts` | API 调用函数                  |
| 1.6  | 前端 `types/post.ts` 扩展为接口类型                                  | 类型定义                      |
| 1.7  | 前端 `useBlogList` composable 改为调用 API                           | 替换硬编码                    |
| 1.8  | 前端删除 `constants/posts.ts` 中的硬编码数据                         | 清理                          |

### Phase 2 — 个人信息（P1）

**目标**：AboutSection 数据从后端获取，支持后台编辑。

| 步骤 | 任务                                        | 产出                 |
| ---- | ------------------------------------------- | -------------------- |
| 2.1  | 后端建表 `blog_profile`，初始化一条默认记录 | DDL + init SQL       |
| 2.2  | 后端实现 profile GET/PUT 接口               | Controller + Service |
| 2.3  | 前端 `api/profile.ts`                       | API 调用函数         |
| 2.4  | 前端 AboutSection 改为接口数据渲染          | 组件改造             |

### Phase 3 — 教育与技能（P2）

**目标**：EducationSection、SkillsSection 数据从后端获取，支持后台编辑。

| 步骤 | 任务                                                                | 产出                 |
| ---- | ------------------------------------------------------------------- | -------------------- |
| 3.1  | 后端建表 `blog_education`、`blog_skill_category`、`blog_skill_item` | DDL + init SQL       |
| 3.2  | 后端实现 education CRUD 接口                                        | Controller + Service |
| 3.3  | 后端实现 skills CRUD 接口                                           | Controller + Service |
| 3.4  | 前端 `api/education.ts`、`api/skill.ts`                             | API 调用函数         |
| 3.5  | 前端 EducationSection、SkillsSection 改为接口数据渲染               | 组件改造             |
| 3.6  | 前端删除 `constants/education.ts`、`constants/skills.ts` 硬编码     | 清理                 |

### Phase 4 — 文章详情页（P1）

**目标**：支持 Markdown 渲染的文章详情页。

| 步骤 | 任务                                                | 产出        |
| ---- | --------------------------------------------------- | ----------- |
| 4.1  | 前端新增 `BlogDetailPage.vue`，Markdown 渲染组件    | 页面 + 路由 |
| 4.2  | 前端调用 `GET /api/blog/posts/slug/{slug}` 获取详情 | API 对接    |
| 4.3  | 浏览量计数（可选：后端在 GET 详情时 +1）            | 统计        |

---

## 6. 权限说明

| 操作                                                   | 鉴权     | 说明             |
| ------------------------------------------------------ | -------- | ---------------- |
| 公开读取（文章列表/详情/分类/标签/个人信息/教育/技能） | 无需登录 | 前台访客可见     |
| 写入操作（创建/更新/删除）                             | 需登录   | 站长本人或 ADMIN |

> 当前设计写入接口仅要求登录态。如需限制为特定角色，可在后端通过角色注解进一步收窄（如 `@PreAuthorize("hasRole('ADMIN')")`）。

---

## 7. 注意事项

1. **slug 自动生成**：创建文章时若不传 slug，后端应根据 title 自动生成（中文标题可用拼音或 ID 兜底）。
2. **readTime 自动计算**：若前端不传 readTime，后端可根据 content 字数估算（中文约 300 字/分钟，英文约 200 词/分钟）。
3. **草稿可见性**：`GET /api/blog/posts` 默认只返回 `is_published = 1` 的文章；已登录用户可通过 query 参数 `includeDraft=true` 查看自己的草稿。
4. **分页上限**：`pageSize` 最大 50，超出时后端截断为 50。
5. **标签去重**：创建标签时 name 唯一；关联文章标签时若标签不存在可考虑自动创建（可选）。
6. **排序规则**：文章列表默认按 `is_top DESC, published_at DESC` 排序。
7. **前端常量清理**：按接口规范，接入真实 API 后必须删除 `constants/` 中的硬编码示例数据，不得保留 `EXAMPLE_*` 或伪装数据。
