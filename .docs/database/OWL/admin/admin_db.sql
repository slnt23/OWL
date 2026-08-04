-- ======================================================
-- 个人探索数据库：ecommerce
-- 数据库版本：v3
-- 模块：admin（管理员模块）
-- MySQL版本：8.x
-- 日志
-- ======================================================

-- 5.1 管理员操作日志表
CREATE TABLE admin_log
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    admin_id    BIGINT NOT NULL COMMENT '管理员ID',
    action      VARCHAR(100) COMMENT '操作类型',
    detail      VARCHAR(255) COMMENT '操作描述',
    ip          VARCHAR(50) COMMENT '操作IP',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_admin_id (admin_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='管理员操作日志表';

-- 7.1 业务操作日志表
CREATE TABLE biz_log
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    module      VARCHAR(64)     NOT NULL COMMENT '业务模块（如：订单、用户）',
    action      VARCHAR(128)    NOT NULL COMMENT '业务动作（如：创建订单、用户登录）',
    user_id     BIGINT       DEFAULT NULL COMMENT '用户ID',
    method      VARCHAR(255)    NOT NULL COMMENT '类名#方法名',
    success     TINYINT(1)      NOT NULL COMMENT '是否成功：1=成功，0=失败',
    cost        BIGINT          NOT NULL COMMENT '耗时（毫秒）',
    error_msg   VARCHAR(512) DEFAULT NULL COMMENT '错误信息（失败时记录）',
    trace_id    VARCHAR(64)  DEFAULT NULL COMMENT '链路追踪ID',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_create_time (create_time),
    KEY idx_user_id (user_id),
    KEY idx_module_action (module, action),
    KEY idx_success (success)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='业务操作日志表';