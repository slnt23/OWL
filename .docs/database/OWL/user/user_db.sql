-- ======================================================
-- 个人探索数据库：ecommerce
-- 数据库版本：v3
-- 模块：user（用户模块）
-- MySQL版本：8.x
-- ======================================================


-- 1.1 角色表
CREATE TABLE role
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    role_name   VARCHAR(100) UNIQUE NOT NULL COMMENT '角色名称',  #后续改为枚举类型
    description VARCHAR(255) COMMENT '描述',
    enabled     TINYINT(1) DEFAULT 1 COMMENT '是否启用：1=启用，0=禁用',
    create_time DATETIME   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色表';


-- 1.2 用户表
CREATE TABLE user
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_code   VARCHAR(50) UNIQUE NOT NULL COMMENT '账号编号',
    username    VARCHAR(50)        NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(100)       COMMENT '密码（加密存储）',
    phone       VARCHAR(20) UNIQUE COMMENT '手机号',
    email       VARCHAR(50) COMMENT '邮箱',
    avatar_url  VARCHAR(2048) COMMENT '头像URL',
    nickname    VARCHAR(50) COMMENT '昵称',
    remark      VARCHAR(255) COMMENT '备注',
    role_id     BIGINT             NOT NULL DEFAULT 0 COMMENT '角色ID',
    status      TINYINT                     DEFAULT 0 COMMENT '状态：0=正常，1=封禁',
    create_time DATETIME                    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (role_id) references role (id) on delete restrict,
    index idx_role (role_id),
    UNIQUE KEY uk_email (email)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

# ALTER TABLE user
#     ADD remark VARCHAR(255) COMMENT '备注';

# alter table user
# modify column avatar varchar(2048) comment '头像URL';

-- 1.3 用户收货地址表
CREATE TABLE user_address
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id        BIGINT NOT NULL COMMENT '用户ID',
    receiver_name  VARCHAR(50) COMMENT '收件人姓名',
    receiver_phone VARCHAR(20) COMMENT '收件人电话',
    province       VARCHAR(50) COMMENT '省',
    city           VARCHAR(50) COMMENT '城',
    district       VARCHAR(50) COMMENT '县',
    detail         VARCHAR(255) COMMENT '详细地址',
    is_default     TINYINT  DEFAULT 0 COMMENT '是否默认地址：1=是，0=否',
    create_time    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_user_id (user_id),
    FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户收货地址表';



-- 1.000 用户角色关联表
# CREATE TABLE user_role
# (
#     id      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
#     user_id BIGINT NOT NULL COMMENT '用户ID',
#     role_id BIGINT NOT NULL COMMENT '角色ID',
#
#     FOREIGN KEY (user_id) REFERENCES user (id) ON DELETE CASCADE,
#     FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
# ) ENGINE = InnoDB
#   DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联表';

-- 1.4 用户操作日志表
CREATE TABLE user_log
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT DEFAULT NULL COMMENT '用户ID',
    module      VARCHAR(64)  NOT NULL COMMENT '业务模块',
    action      VARCHAR(128) NOT NULL COMMENT '操作动作',
    method      VARCHAR(255) DEFAULT NULL COMMENT '类名#方法名',
    success     TINYINT(1)   DEFAULT NULL COMMENT '是否成功：1=成功，0=失败',
    cost        BIGINT       DEFAULT NULL COMMENT '耗时（毫秒）',
    error_msg   VARCHAR(512) DEFAULT NULL COMMENT '错误信息',
    trace_id    VARCHAR(64)  DEFAULT NULL COMMENT '链路追踪ID',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user_id (user_id),
    INDEX idx_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户操作日志表';
