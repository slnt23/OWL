-- ======================================================
-- OWL 数据库：user 模块 - 博客表
-- 模块：user
-- MySQL 版本：8.4
-- 说明：博客文章、设置、教育经历、技能分类/条目由 user 模块维护。
--       表名沿用 blog_ 前缀，与前端 src/modules/blog 语义对齐。
--       依赖 user 模块的 user_account 表（blog_post.user_id）。
-- ======================================================

-- ------------------------------------------------------
-- 1. 博客文章表
-- ------------------------------------------------------
CREATE TABLE blog_post
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '所属用户ID',
    title       VARCHAR(255)    NOT NULL COMMENT '文章标题',
    excerpt     VARCHAR(500) COMMENT '文章摘要',
    content     LONGTEXT COMMENT '文章正文（Markdown）',
    cover_url   VARCHAR(500) COMMENT '封面图片URL',
    tags        JSON COMMENT '标签数组',
    status      TINYINT         NOT NULL DEFAULT 0 COMMENT '状态：0=草稿，1=已发布',
    sort_order  INT             NOT NULL DEFAULT 0 COMMENT '排序权重，越大越靠前',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除，1=已删除',

    PRIMARY KEY (id),
    KEY idx_blog_post_user_id (user_id),
    KEY idx_blog_post_status (status),
    KEY idx_blog_post_sort_order (sort_order),
    KEY idx_blog_post_create_time (create_time),
    CONSTRAINT fk_blog_post_user_id FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客文章表';

-- ------------------------------------------------------
-- 2. 博客设置表
-- ------------------------------------------------------
CREATE TABLE blog_settings
(
    id           BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id      BIGINT UNSIGNED NOT NULL COMMENT '所属用户ID（一对一）',
    tag_line     VARCHAR(255) COMMENT '标签行（支持HTML）',
    bio          JSON COMMENT '简介段落数组',
    location     VARCHAR(100) COMMENT '位置信息',
    github_url   VARCHAR(255) COMMENT 'GitHub 链接',
    codetime_url VARCHAR(500) COMMENT 'CodeTime 统计 Badge URL',
    poem         VARCHAR(255) COMMENT '诗句/座右铭',
    update_time  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_blog_settings_user_id (user_id),
    CONSTRAINT fk_blog_settings_user_id FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客设置表';

-- ------------------------------------------------------
-- 3. 教育经历表
-- ------------------------------------------------------
CREATE TABLE blog_education
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '所属用户ID',
    school      VARCHAR(255)    NOT NULL COMMENT '学校名称',
    degree      VARCHAR(255) COMMENT '学位/专业',
    `period`      VARCHAR(100) COMMENT '时间段',
    sort_order  INT             NOT NULL DEFAULT 0 COMMENT '排序权重',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_blog_education_user_id (user_id),
    KEY idx_blog_education_sort_order (sort_order),
    CONSTRAINT fk_blog_education_user_id FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客教育经历表';

-- ------------------------------------------------------
-- 4. 技能分类表
-- ------------------------------------------------------
CREATE TABLE blog_skill_category
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT UNSIGNED NOT NULL COMMENT '所属用户ID',
    category    VARCHAR(50)     NOT NULL COMMENT '分类名称',
    sort_order  INT             NOT NULL DEFAULT 0 COMMENT '排序权重',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_blog_skill_category_user_id (user_id),
    UNIQUE KEY uk_blog_skill_category_user_category (user_id, category),
    CONSTRAINT fk_blog_skill_category_user_id FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客技能分类表';

-- ------------------------------------------------------
-- 5. 技能项表
-- ------------------------------------------------------
CREATE TABLE blog_skill_item
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    category_id BIGINT UNSIGNED NOT NULL COMMENT '所属分类ID',
    item_name   VARCHAR(50)     NOT NULL COMMENT '技能名称',
    sort_order  INT             NOT NULL DEFAULT 0 COMMENT '排序权重',
    create_time DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_blog_skill_item_category_id (category_id),
    UNIQUE KEY uk_blog_skill_item_category_item (category_id, item_name),
    CONSTRAINT fk_blog_skill_item_category_id FOREIGN KEY (category_id) REFERENCES blog_skill_category (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '博客技能项表';