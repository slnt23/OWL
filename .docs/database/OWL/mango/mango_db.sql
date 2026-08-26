-- ======================================================
-- OWL 数据库：mango 模块 canonical DDL
-- 模块：mango（个人博客，代号 mango）
-- MySQL 版本：8.4
-- 说明：博客文章/分类/标签/个人信息/教育/技能由 mango 模块维护。
--       表名沿用 blog_ 前缀，与前端 src/modules/blog 语义对齐。
--       依赖 user 模块的 user_account 表（blog_post.created_by）。
-- ======================================================

-- ------------------------------------------------------
-- 1. 文章分类表
-- ------------------------------------------------------
CREATE TABLE blog_category
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(50) NOT NULL COMMENT '分类名称',
    slug        VARCHAR(50) COMMENT 'URL 标识',
    sort_order  INT         NOT NULL DEFAULT 0 COMMENT '排序序号，数值越小越靠前',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_blog_category_name (name),
    UNIQUE KEY uk_blog_category_slug (slug)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客文章分类表';

-- ------------------------------------------------------
-- 2. 标签表
-- ------------------------------------------------------
CREATE TABLE blog_tag
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    name        VARCHAR(50) NOT NULL COMMENT '标签名称',
    slug        VARCHAR(50) COMMENT 'URL 标识',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_blog_tag_name (name),
    UNIQUE KEY uk_blog_tag_slug (slug)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客标签表';

-- ------------------------------------------------------
-- 3. 文章表
-- ------------------------------------------------------
CREATE TABLE blog_post
(
    id           BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    title        VARCHAR(200)    NOT NULL COMMENT '标题',
    excerpt      VARCHAR(500) COMMENT '摘要/简介',
    content      LONGTEXT COMMENT '正文（Markdown 原文）',
    cover_image  VARCHAR(500) COMMENT '封面图 URL',
    slug         VARCHAR(200)    NOT NULL COMMENT 'URL 友好标识，如 xv6-os-lab-part8',
    category_id  BIGINT UNSIGNED COMMENT '所属分类ID',
    lang         VARCHAR(10)     NOT NULL DEFAULT 'zh' COMMENT '语言标识：zh / en',
    read_time    INT COMMENT '预估阅读时长（分钟）',
    is_published TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否发布：0=草稿，1=已发布',
    is_top       TINYINT(1)      NOT NULL DEFAULT 0 COMMENT '是否置顶：0=否，1=是',
    view_count   INT             NOT NULL DEFAULT 0 COMMENT '浏览次数',
    like_count   INT             NOT NULL DEFAULT 0 COMMENT '点赞数',
    publish_time DATETIME COMMENT '发布时间（前端展示用）',
    created_by   BIGINT UNSIGNED NOT NULL COMMENT '作者用户ID',
    create_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_blog_post_slug (slug),
    KEY idx_blog_post_category_id (category_id),
    KEY idx_blog_post_publish_time (publish_time),
    KEY idx_blog_post_is_published (is_published),
    KEY idx_blog_post_created_by (created_by),
    CONSTRAINT fk_blog_post_category FOREIGN KEY (category_id) REFERENCES blog_category (id) ON DELETE SET NULL,
    CONSTRAINT fk_blog_post_created_by FOREIGN KEY (created_by) REFERENCES user_account (id) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客文章表';

-- ------------------------------------------------------
-- 4. 文章-标签关联表
-- ------------------------------------------------------
CREATE TABLE blog_post_tag
(
    id      BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    post_id BIGINT UNSIGNED NOT NULL COMMENT '文章ID',
    tag_id  BIGINT UNSIGNED NOT NULL COMMENT '标签ID',

    PRIMARY KEY (id),
    UNIQUE KEY uk_blog_post_tag_post_tag (post_id, tag_id),
    KEY idx_blog_post_tag_tag_id (tag_id),
    CONSTRAINT fk_blog_post_tag_post FOREIGN KEY (post_id) REFERENCES blog_post (id) ON DELETE CASCADE,
    CONSTRAINT fk_blog_post_tag_tag FOREIGN KEY (tag_id) REFERENCES blog_tag (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客文章-标签关联表';

-- ------------------------------------------------------
-- 5. 站长个人信息表（单行配置表）
-- ------------------------------------------------------
CREATE TABLE blog_profile
(
    id           BIGINT UNSIGNED NOT NULL COMMENT '主键ID，固定为 1',
    avatar_url   VARCHAR(500) COMMENT '头像 URL',
    name         VARCHAR(50)  NOT NULL COMMENT '显示名称',
    tagline      VARCHAR(200) COMMENT '一行标签，如 Developer / Designer',
    bio          TEXT COMMENT '个人简介（支持 Markdown）',
    location     VARCHAR(100) COMMENT '所在地',
    github_url   VARCHAR(200) COMMENT 'GitHub 链接',
    website_url  VARCHAR(200) COMMENT '个人网站',
    email        VARCHAR(100) COMMENT '联系邮箱',
    codetime_uid VARCHAR(50) COMMENT 'CodeTime UID（用于徽章）',
    create_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客站长个人信息表';

-- ------------------------------------------------------
-- 6. 教育经历表
-- ------------------------------------------------------
CREATE TABLE blog_education
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    school      VARCHAR(100) NOT NULL COMMENT '学校名称',
    degree      VARCHAR(200) NOT NULL COMMENT '学位/专业',
    `period`    VARCHAR(100) NOT NULL COMMENT '时间段',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序序号，数值越小越靠前',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客教育经历表';

-- ------------------------------------------------------
-- 7. 技能分类表
-- ------------------------------------------------------
CREATE TABLE blog_skill_category
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    category    VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort_order  INT         NOT NULL DEFAULT 0 COMMENT '排序序号，数值越小越靠前',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客技能分类表';

-- ------------------------------------------------------
-- 8. 技能条目表
-- ------------------------------------------------------
CREATE TABLE blog_skill_item
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    category_id BIGINT UNSIGNED NOT NULL COMMENT '所属技能分类ID',
    name        VARCHAR(50)     NOT NULL COMMENT '技能名称',
    sort_order  INT             NOT NULL DEFAULT 0 COMMENT '分类内排序序号，数值越小越靠前',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_blog_skill_item_category_id (category_id),
    CONSTRAINT fk_blog_skill_item_category FOREIGN KEY (category_id) REFERENCES blog_skill_category (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客技能条目表';