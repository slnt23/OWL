-- ======================================================
-- 电商系统数据库：pitaya
-- 数据库版本：v1（已废弃，请使用第二版）
-- MySQL版本：8.x
-- 说明：此版本已被第二版替代，仅作历史参考
-- ======================================================

-- CREATE DATABASE pitaya;
-- USE pitaya;

-- ======================================================
-- 1. 商品模块
-- ======================================================

-- ------------------------------------------------------
-- 1.1 商品类别表
-- ------------------------------------------------------
CREATE TABLE product_category (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    category_code   VARCHAR(50) UNIQUE NOT NULL          COMMENT '类别编号',
    category_name   VARCHAR(100) NOT NULL                COMMENT '类别名称',
    description     VARCHAR(255)                         COMMENT '描述',
    enabled         TINYINT(1) DEFAULT 1                 COMMENT '是否启用：1=启用，0=禁用',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类别表';

-- ------------------------------------------------------
-- 1.2 商品表
-- ------------------------------------------------------
CREATE TABLE product (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    product_code    VARCHAR(50) UNIQUE NOT NULL          COMMENT '商品编号',
    product_name    VARCHAR(200) NOT NULL                COMMENT '商品名称',
    category_id     BIGINT NOT NULL                      COMMENT '类别ID',
    price           DECIMAL(10,2) NOT NULL               COMMENT '价格',
    stock           INT DEFAULT 0                        COMMENT '库存',
    status          TINYINT(1) DEFAULT 1                 COMMENT '状态：1=上架，0=下架',
    remark          VARCHAR(255)                         COMMENT '备注',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
    update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    FOREIGN KEY (category_id) REFERENCES product_category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';


-- ======================================================
-- 2. 用户模块
-- ======================================================

-- ------------------------------------------------------
-- 2.1 用户表
-- ------------------------------------------------------
CREATE TABLE user (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    user_code       VARCHAR(50) UNIQUE NOT NULL          COMMENT '账号编号',
    username        VARCHAR(100) NOT NULL                COMMENT '姓名',
    email           VARCHAR(100) UNIQUE NOT NULL         COMMENT '邮箱',
    password        VARCHAR(255) NOT NULL                COMMENT '密码（加密存储）',
    remark          VARCHAR(255)                         COMMENT '备注',
    enabled         TINYINT(1) DEFAULT 1                 COMMENT '是否启用：1=启用，0=禁用',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '添加时间',
    last_login      DATETIME                             COMMENT '最后登录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ------------------------------------------------------
-- 2.2 角色表
-- ------------------------------------------------------
CREATE TABLE role (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    role_name       VARCHAR(100) UNIQUE NOT NULL         COMMENT '角色名称',
    description     VARCHAR(255)                         COMMENT '描述',
    enabled         TINYINT(1) DEFAULT 1                 COMMENT '是否启用：1=启用，0=禁用',
    create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ------------------------------------------------------
-- 2.3 用户角色关联表
-- ------------------------------------------------------
CREATE TABLE user_role (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    user_id         BIGINT NOT NULL                      COMMENT '用户ID',
    role_id         BIGINT NOT NULL                      COMMENT '角色ID',

    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


-- ======================================================
-- 3. 订单模块
-- ======================================================

-- ------------------------------------------------------
-- 3.1 订单表
-- ------------------------------------------------------
CREATE TABLE orders (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    order_code      VARCHAR(100) UNIQUE NOT NULL         COMMENT '订单编号',
    user_id         BIGINT NOT NULL                      COMMENT '下单用户ID',
    status          VARCHAR(50) DEFAULT '待支付'         COMMENT '订单状态：待支付/已支付/已发货/已完成/已取消',
    total_amount    DECIMAL(10,2) DEFAULT 0              COMMENT '总金额',
    order_time      DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '下单时间',
    ship_time       DATETIME                             COMMENT '发货时间',
    cancel_time     DATETIME                             COMMENT '取消时间',
    finish_time     DATETIME                             COMMENT '完成时间',

    FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ------------------------------------------------------
-- 3.2 订单详情表
-- ------------------------------------------------------
CREATE TABLE order_detail (
    id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
    detail_code     VARCHAR(100) UNIQUE NOT NULL         COMMENT '订单详情编号',
    order_id        BIGINT NOT NULL                      COMMENT '所属订单ID',
    product_id      BIGINT NOT NULL                      COMMENT '商品ID',
    quantity        INT DEFAULT 1                        COMMENT '数量',
    unit_price      DECIMAL(10,2) NOT NULL               COMMENT '单价',
    total_price     DECIMAL(10,2) GENERATED ALWAYS AS (quantity * unit_price) STORED COMMENT '总价（自动计算）',
    remark          VARCHAR(255)                         COMMENT '备注',

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';


-- ======================================================
-- 4. 统计模块
-- ======================================================

-- ------------------------------------------------------
-- 4.1 每日数据统计表
-- ------------------------------------------------------
CREATE TABLE stats_daily (
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stat_date               DATE NOT NULL                 COMMENT '统计日期',
    order_count             INT DEFAULT 0                 COMMENT '订单数',
    new_user_count          INT DEFAULT 0                 COMMENT '新增用户数',
    active_product_count    INT DEFAULT 0                 COMMENT '上架商品数',
    create_time             DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日数据统计表';