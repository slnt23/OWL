-- ======================================================
-- OWL 数据库：log 模块 canonical DDL
-- 模块：log
-- MySQL 版本：8.4
-- 说明：日志表统一由 log 模块维护，其他模块只读。
-- ======================================================

-- ------------------------------------------------------
-- 1. 用户操作日志表
-- ------------------------------------------------------
CREATE TABLE user_log
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT UNSIGNED COMMENT '用户ID',
    module      VARCHAR(64)  NOT NULL COMMENT '业务模块',
    action      VARCHAR(128) NOT NULL COMMENT '操作动作',
    method      VARCHAR(255) COMMENT '类名#方法名',
    success     TINYINT(1) COMMENT '是否成功：1=成功，0=失败',
    cost        BIGINT COMMENT '耗时（毫秒）',
    error_msg   VARCHAR(512) COMMENT '错误信息',
    trace_id    VARCHAR(64) COMMENT '链路追踪ID',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_user_log_user_id (user_id),
    KEY idx_user_log_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户操作日志表';

-- ------------------------------------------------------
-- 2. 业务操作日志表
-- ------------------------------------------------------
CREATE TABLE biz_log
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    module      VARCHAR(64)  NOT NULL COMMENT '业务模块',
    action      VARCHAR(128) NOT NULL COMMENT '业务动作',
    user_id     BIGINT UNSIGNED COMMENT '用户ID',
    method      VARCHAR(255) NOT NULL COMMENT '类名#方法名',
    success     TINYINT(1)   NOT NULL COMMENT '是否成功：1=成功，0=失败',
    cost        BIGINT       NOT NULL COMMENT '耗时（毫秒）',
    error_msg   VARCHAR(512) COMMENT '错误信息',
    trace_id    VARCHAR(64) COMMENT '链路追踪ID',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_biz_log_create_time (create_time),
    KEY idx_biz_log_user_id (user_id),
    KEY idx_biz_log_module_action (module, action),
    KEY idx_biz_log_success (success)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '业务操作日志表';

-- ------------------------------------------------------
-- 3. 管理员操作日志表
-- ------------------------------------------------------
CREATE TABLE admin_log
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    admin_id    BIGINT UNSIGNED NOT NULL COMMENT '管理员ID',
    action      VARCHAR(100) COMMENT '操作类型',
    detail      VARCHAR(255) COMMENT '操作描述',
    ip          VARCHAR(50) COMMENT '操作IP',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_admin_log_admin_id (admin_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '管理员操作日志表';
