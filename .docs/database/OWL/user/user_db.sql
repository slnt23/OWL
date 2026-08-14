-- ======================================================
-- OWL 数据库：user 模块 canonical DDL
-- 模块：user
-- MySQL 版本：8.4
-- 说明：role / user / user_address 由 user 模块维护，
--       api 与 administration 复用 role 表。
-- ======================================================

-- ------------------------------------------------------
-- 1. 角色表
-- ------------------------------------------------------
CREATE TABLE role
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    role_name   VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(255) COMMENT '角色描述',
    enabled     TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '是否启用：1=启用，0=禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_role_name (role_name)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '角色表';

-- ------------------------------------------------------
-- 2. 用户表
-- ------------------------------------------------------
CREATE TABLE user
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_code   VARCHAR(64)      NOT NULL COMMENT '账号编号',
    username    VARCHAR(50)      NOT NULL COMMENT '用户名',
    password    VARCHAR(100) COMMENT '密码（加密存储）',
    phone       VARCHAR(20) COMMENT '手机号',
    email       VARCHAR(50)      NOT NULL COMMENT '邮箱',
    avatar_url  VARCHAR(2048) COMMENT '头像URL',
    nickname    VARCHAR(50) COMMENT '昵称',
    remark      VARCHAR(255) COMMENT '备注',
    role_name   VARCHAR(100)     NOT NULL COMMENT '角色名称',
    status      TINYINT(1)       NOT NULL DEFAULT 0 COMMENT '状态：0=正常，1=封禁',
    create_time DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_user_code (user_code),
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone),
    UNIQUE KEY uk_email (email),
    KEY idx_role_name (role_name),
    CONSTRAINT fk_user_role FOREIGN KEY (role_name) REFERENCES role (role_name) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户表';

-- ------------------------------------------------------
-- 3. 用户收货地址表
-- ------------------------------------------------------
CREATE TABLE user_address
(
    id             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id        BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
    receiver_name  VARCHAR(50) COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) COMMENT '收件人电话',
    province       VARCHAR(50) COMMENT '省',
    city           VARCHAR(50) COMMENT '市',
    district       VARCHAR(50) COMMENT '区县',
    detail         VARCHAR(255) COMMENT '详细地址',
    is_default     TINYINT(1)     NOT NULL DEFAULT 0 COMMENT '是否默认地址：1=是，0=否',
    create_time    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    CONSTRAINT fk_user_address_user FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户收货地址表';
