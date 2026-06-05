-- ======================================================
-- 个人探索数据库：commence
-- 价格系统数据库表结构
-- 版本: v1.0
-- 创建时间: 2026-01-15
-- ======================================================

-- ------------------------------------------------------
-- 1. 价格系统分类表
-- 说明: 用于管理价格相关的商品/服务分类（支持层级）
-- ------------------------------------------------------
CREATE TABLE price_category
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    parent_id     BIGINT   DEFAULT NULL COMMENT '父分类ID（顶级为NULL）',
    category_name VARCHAR(64) NOT NULL COMMENT '分类名称',
    category_code VARCHAR(64) NOT NULL UNIQUE COMMENT '分类唯一编码',
    level         INT         NOT NULL COMMENT '层级（1=一级分类，2=二级分类...）',
    sort_order    INT      DEFAULT 0 COMMENT '排序值（越小越靠前）',
    status        TINYINT  DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    created_at    DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_parent (parent_id),
    INDEX idx_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='价格系统分类表';


-- ------------------------------------------------------
-- 2. 被定价物品表
-- 说明: 定义需要定价的所有物品（成品、原料、服务等）
-- ------------------------------------------------------
CREATE TABLE price_item
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    item_code      VARCHAR(64)  NOT NULL UNIQUE COMMENT '物品唯一编码（系统内部识别）',
    item_name      VARCHAR(128) NOT NULL COMMENT '物品名称',
    category_id    BIGINT       NOT NULL COMMENT '分类ID（关联price_category.id）',
    unit           VARCHAR(32)  NOT NULL COMMENT '计价单位（如：升、克、吨、次）',
    specification  VARCHAR(128) DEFAULT NULL COMMENT '规格描述（如：500ml装）',
    description    TEXT COMMENT '备注说明',
    ref_product_id BIGINT       DEFAULT NULL COMMENT '关联商城商品ID（可为空）',
    status         TINYINT      DEFAULT 1 COMMENT '状态：1-启用，0-停用',
    created_at     DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at     DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_category (category_id),
    INDEX idx_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='被定价物品表';


-- ------------------------------------------------------
-- 3. 价格来源表
-- 说明: 记录价格数据的来源渠道，用于数据溯源和可信度评估
-- ------------------------------------------------------
CREATE TABLE price_source
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    source_name       VARCHAR(64) NOT NULL COMMENT '来源名称（如：国家统计局、XX批发市场）',
    source_type       VARCHAR(32) NOT NULL COMMENT '来源类型：API-接口，CRAWLER-爬虫，MANUAL-人工录入',
    source_url        VARCHAR(255) DEFAULT NULL COMMENT '来源地址',
    reliability_level INT          DEFAULT 3 COMMENT '可靠等级1-5（5为最高）',
    remark            VARCHAR(255) DEFAULT NULL COMMENT '备注',
    status            TINYINT      DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    created_at        DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_status (status),
    INDEX idx_type (source_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='价格来源表';


-- ------------------------------------------------------
-- 4. 地理位置表
-- 说明: 存储区域/地点信息，支持层级结构和地理坐标
-- ------------------------------------------------------
CREATE TABLE geo_location
(
    id            BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    location_name VARCHAR(128)   NOT NULL COMMENT '地点名称（如：北京市、朝阳区）',
    location_type VARCHAR(32)    NOT NULL COMMENT '类型：COUNTRY-国家，CITY-城市，DISTRICT-区县，STORE-门店',
    parent_id     BIGINT      DEFAULT NULL COMMENT '父级区域ID（关联本表id）',
    longitude     DECIMAL(10, 7) NOT NULL COMMENT '经度（GCJ-02坐标系）',
    latitude      DECIMAL(10, 7) NOT NULL COMMENT '纬度（GCJ-02坐标系）',
    geo_hash      VARCHAR(12) DEFAULT NULL COMMENT 'GeoHash编码（用于快速地理位置检索）',
    status        TINYINT     DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
    created_at    DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_parent (parent_id),
    INDEX idx_geo (longitude, latitude)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='地理位置表';


-- ------------------------------------------------------
-- 5. 价格记录表（时间序列数据）
-- 说明: 核心价格数据表，存储各时间点的价格记录
-- ------------------------------------------------------
CREATE TABLE price_record
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    item_id        BIGINT         NOT NULL COMMENT '物品ID（关联price_item.id）',
    location_id    BIGINT         NOT NULL COMMENT '位置ID（关联geo_location.id）',
    price          DECIMAL(18, 6) NOT NULL COMMENT '价格数值',
    currency       VARCHAR(8)     NOT NULL COMMENT '币种（CNY-人民币，USD-美元）',
    price_unit     VARCHAR(32)    NOT NULL COMMENT '价格单位（如：元/升、美元/吨）',
    source_id      BIGINT         NOT NULL COMMENT '来源ID（关联price_source.id）',
    effective_time DATETIME       NOT NULL COMMENT '生效时间（价格开始生效的时间点）',
    expire_time    DATETIME      DEFAULT NULL COMMENT '失效时间（NULL表示长期有效）',
    record_time    DATETIME       NOT NULL COMMENT '记录时间（数据录入系统的时间）',
    confidence     DECIMAL(5, 2) DEFAULT 100.00 COMMENT '可信度（0-100，数值越高越可信）',
    created_at     DATETIME      DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    -- 索引
    INDEX idx_item (item_id),
    INDEX idx_location (location_id),
    INDEX idx_time (effective_time),
    INDEX idx_source (source_id),

    -- 外键约束
    CONSTRAINT fk_price_item
        FOREIGN KEY (item_id) REFERENCES price_item (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_price_location
        FOREIGN KEY (location_id) REFERENCES geo_location (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_price_source
        FOREIGN KEY (source_id) REFERENCES price_source (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='价格记录表（时间序列数据）';

-- ------------------------------------------------------
-- 6. 价格（对象文件存储）表
-- 说明: 存储图片，视频，文档
-- ------------------------------------------------------
CREATE TABLE price_item_media
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT comment '主键ID',
    item_id    BIGINT                         NOT NULL COMMENT '物品ID(关联price_item)',
    media_type ENUM ('IMAGE', 'VIDEO', 'DOC') NOT NULL COMMENT '媒体类型',
    url        VARCHAR(255)                   NOT NULL COMMENT 'URL',
    sort_order INT      DEFAULT 0 COMMENT '排序',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (item_id) REFERENCES price_item (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='价格文件扩展表';
