-- ======================================================
-- OWL 数据库：user 模块 - 画廊表
-- 模块：user
-- MySQL 版本：8.4
-- 说明：user_gallery 由 user 模块维护，每个用户拥有自己的画廊项。
-- ======================================================

-- ------------------------------------------------------
-- 画廊表
-- ------------------------------------------------------
CREATE TABLE user_gallery
(
    id            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id       BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    title         VARCHAR(100)   NOT NULL COMMENT '标题',
    description   VARCHAR(500) COMMENT '描述',
    image_url     VARCHAR(2048)  NOT NULL COMMENT '大图URL',
    thumbnail_url VARCHAR(2048) COMMENT '缩略图URL',
    sort_order    INT            NOT NULL DEFAULT 0 COMMENT '排序序号，数值越小越靠前',
    create_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_user_gallery_user_id (user_id),
    KEY idx_user_gallery_sort_order (sort_order),
    CONSTRAINT fk_user_gallery_user_id FOREIGN KEY (user_id) REFERENCES user_account (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '画廊表';