-- ======================================================
-- 电商系统数据库：ecommerce
-- 数据库版本：v2
-- 项目：pitaya
-- MySQL版本：8.x
-- 创建时间：2026-01-15
-- ======================================================

-- CREATE DATABASE IF NOT EXISTS ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
-- USE ecommerce;

-- ======================================================
-- 1. 用户模块
-- ======================================================

-- ------------------------------------------------------
-- 1.1 用户表
-- ------------------------------------------------------
CREATE TABLE user (
                      id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
                      user_code       VARCHAR(50) UNIQUE NOT NULL          COMMENT '账号编号',
                      username        VARCHAR(50) NOT NULL UNIQUE          COMMENT '用户名',
                      password        VARCHAR(100) NOT NULL                COMMENT '密码（加密存储）',
                      phone           VARCHAR(20) UNIQUE                   COMMENT '手机号',
                      email           VARCHAR(50)                          COMMENT '邮箱',
                      avatar          VARCHAR(255)                         COMMENT '头像URL',
                      nickname        VARCHAR(50)                          COMMENT '昵称',
                      role            TINYINT DEFAULT 0                    COMMENT '角色：0=用户，1=商家，2=管理员',
                      status          TINYINT DEFAULT 0                    COMMENT '状态：0=正常，1=封禁',
                      create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
                      update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

ALTER TABLE user ADD remark VARCHAR(255) COMMENT '备注';

-- ------------------------------------------------------
-- 1.2 用户收货地址表
-- ------------------------------------------------------
CREATE TABLE user_address (
                              id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
                              user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                              receiver_name   VARCHAR(50)                          COMMENT '收件人姓名',
                              receiver_phone  VARCHAR(20)                          COMMENT '收件人电话',
                              province        VARCHAR(50)                          COMMENT '省',
                              city            VARCHAR(50)                          COMMENT '城',
                              district        VARCHAR(50)                          COMMENT '县',
                              detail          VARCHAR(255)                         COMMENT '详细地址',
                              is_default      TINYINT DEFAULT 0                    COMMENT '是否默认地址：1=是，0=否',
                              create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',

                              INDEX idx_user_id (user_id),
                              FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址表';

-- ------------------------------------------------------
-- 1.3 角色表
-- ------------------------------------------------------
CREATE TABLE role (
                      id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
                      role_name       VARCHAR(100) UNIQUE NOT NULL         COMMENT '角色名称',
                      description     VARCHAR(255)                         COMMENT '描述',
                      enabled         TINYINT(1) DEFAULT 1                 COMMENT '是否启用：1=启用，0=禁用',
                      create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ------------------------------------------------------
-- 1.4 用户角色关联表
-- ------------------------------------------------------
CREATE TABLE user_role (
                           id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
                           user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                           role_id         BIGINT NOT NULL                      COMMENT '角色ID',

                           FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
                           FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';


-- ======================================================
-- 2. 商品模块
-- ======================================================

-- ------------------------------------------------------
-- 2.1 商品分类表
-- ------------------------------------------------------
CREATE TABLE category (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '分类ID',
                          parent_id       BIGINT DEFAULT 0                     COMMENT '父分类ID（0表示顶级分类）',
                          name            VARCHAR(100) NOT NULL                COMMENT '分类名称',
                          level           TINYINT DEFAULT 1                    COMMENT '分类层级：1=一级，2=二级，3=三级',
                          sort            INT DEFAULT 0                        COMMENT '排序字段（越小越靠前）',
                          description     VARCHAR(255)                         COMMENT '描述',
                          enabled         TINYINT(1) DEFAULT 1                 COMMENT '是否启用：1=启用，0=禁用',
                          create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
                          update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- ------------------------------------------------------
-- 2.2 商品表
-- ------------------------------------------------------
CREATE TABLE product (
                         id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '商品ID',
                         category_id     BIGINT NOT NULL                      COMMENT '分类ID',
                         seller_id       BIGINT NOT NULL                      COMMENT '商家ID',
                         name            VARCHAR(100) NOT NULL                COMMENT '商品名称',
                         description     TEXT                                 COMMENT '商品描述',
                         price           DECIMAL(10,2) NOT NULL               COMMENT '单价',
                         stock           INT NOT NULL                         COMMENT '库存',
                         status          TINYINT DEFAULT 0                    COMMENT '状态：0=上架，1=下架',
                         cover_img       VARCHAR(255)                         COMMENT '封面图URL',
                         create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',
                         update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                         INDEX idx_category_id (category_id),
                         INDEX idx_seller_id (seller_id),
                         INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- ------------------------------------------------------
-- 2.3 商品图片表
-- ------------------------------------------------------
CREATE TABLE product_image (
                               id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '图片ID',
                               product_id      BIGINT NOT NULL                      COMMENT '商品ID',
                               image_url       VARCHAR(255) NOT NULL                COMMENT '图片URL',
                               sort            INT DEFAULT 0                        COMMENT '排序（越小越靠前）',

                               INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';


-- ======================================================
-- 3. 购物车模块
-- ======================================================

-- ------------------------------------------------------
-- 3.1 购物车表
-- ------------------------------------------------------
CREATE TABLE cart_item (
                           id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '购物车项ID',
                           user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                           product_id      BIGINT NOT NULL                      COMMENT '商品ID',
                           quantity        INT NOT NULL DEFAULT 1               COMMENT '数量',
                           checked         TINYINT DEFAULT 1                    COMMENT '是否勾选：1=是，0=否',
                           create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '添加时间',
                           update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                           INDEX idx_user_id (user_id),
                           INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';


-- ======================================================
-- 4. 订单模块
-- ======================================================

-- ------------------------------------------------------
-- 4.1 订单主表
-- ------------------------------------------------------
CREATE TABLE order_mast (
                            id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '订单ID',
                            order_no        VARCHAR(64) NOT NULL UNIQUE          COMMENT '订单编号',
                            user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                            total_amount    DECIMAL(10,2) NOT NULL               COMMENT '订单总金额',
                            pay_status      TINYINT DEFAULT 0                    COMMENT '支付状态：0=未支付，1=已支付',
                            order_status    TINYINT DEFAULT 0                    COMMENT '订单状态：0=待支付，1=待发货，2=待收货，3=已完成，4=已取消',
                            pay_time        DATETIME                             COMMENT '支付时间',
                            delivery_time   DATETIME                             COMMENT '发货时间',
                            finish_time     DATETIME                             COMMENT '完成时间',
                            address_snapshot TEXT                                COMMENT '收货地址快照（JSON格式）',
                            create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '下单时间',
                            update_time     DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                            INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- ------------------------------------------------------
-- 4.2 订单明细表
-- ------------------------------------------------------
CREATE TABLE order_detail (
                              id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '订单明细ID',
                              order_id        BIGINT NOT NULL                      COMMENT '订单ID',
                              product_id      BIGINT NOT NULL                      COMMENT '商品ID',
                              product_name    VARCHAR(100) NOT NULL                COMMENT '商品名（下单时快照）',
                              product_image   VARCHAR(255)                         COMMENT '商品图片（快照）',
                              unit_price      DECIMAL(10,2) NOT NULL               COMMENT '单价',
                              quantity        INT NOT NULL                         COMMENT '数量',
                              remark          TEXT                                 COMMENT '备注',
                              total_price     DECIMAL(10,2) NOT NULL               COMMENT '小计',
                              create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',

                              INDEX idx_order_id (order_id),
                              INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';


-- ======================================================
-- 5. 管理日志模块
-- ======================================================

-- ------------------------------------------------------
-- 5.1 管理员操作日志表
-- ------------------------------------------------------
CREATE TABLE admin_log (
                           id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '主键ID',
                           admin_id        BIGINT NOT NULL                      COMMENT '管理员ID',
                           action          VARCHAR(100)                         COMMENT '操作类型',
                           detail          VARCHAR(255)                         COMMENT '操作描述',
                           ip              VARCHAR(50)                          COMMENT '操作IP',
                           create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '创建时间',

                           INDEX idx_admin_id (admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员操作日志表';


-- ======================================================
-- 6. 收藏与浏览模块
-- ======================================================

-- ------------------------------------------------------
-- 6.1 收藏表
-- ------------------------------------------------------
CREATE TABLE favorite (
                          id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '收藏ID',
                          user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                          product_id      BIGINT NOT NULL                      COMMENT '商品ID',
                          create_time     DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '收藏时间',

                          UNIQUE KEY uniq_user_product (user_id, product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ------------------------------------------------------
-- 6.2 浏览记录表
-- ------------------------------------------------------
CREATE TABLE view_history (
                              id              BIGINT PRIMARY KEY AUTO_INCREMENT    COMMENT '浏览记录ID',
                              user_id         BIGINT NOT NULL                      COMMENT '用户ID',
                              product_id      BIGINT NOT NULL                      COMMENT '商品ID',
                              view_time       DATETIME DEFAULT CURRENT_TIMESTAMP   COMMENT '浏览时间',

                              INDEX idx_user_id (user_id),
                              INDEX idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='浏览记录表';


-- ======================================================
-- 7. 系统业务日志模块
-- ======================================================

-- ------------------------------------------------------
-- 7.1 业务操作日志表
-- ------------------------------------------------------
CREATE TABLE biz_log (
                         id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         module          VARCHAR(64) NOT NULL                  COMMENT '业务模块（如：订单、用户）',
                         action          VARCHAR(128) NOT NULL                 COMMENT '业务动作（如：创建订单、用户登录）',
                         user_id         BIGINT DEFAULT NULL                   COMMENT '用户ID',
                         method          VARCHAR(255) NOT NULL                 COMMENT '类名#方法名',
                         success         TINYINT(1) NOT NULL                   COMMENT '是否成功：1=成功，0=失败',
                         cost            BIGINT NOT NULL                       COMMENT '耗时（毫秒）',
                         error_msg       VARCHAR(512) DEFAULT NULL             COMMENT '错误信息（失败时记录）',
                         trace_id        VARCHAR(64) DEFAULT NULL              COMMENT '链路追踪ID',
                         create_time     DATETIME DEFAULT CURRENT_TIMESTAMP    COMMENT '创建时间',

                         PRIMARY KEY (id),
                         KEY idx_create_time (create_time),
                         KEY idx_user_id (user_id),
                         KEY idx_module_action (module, action),
                         KEY idx_success (success)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务操作日志表';
