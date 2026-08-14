-- ======================================================
-- OWL 数据库：sugarcane 模块 canonical DDL
-- 模块：sugarcane
-- MySQL 版本：8.4
-- ======================================================

-- ------------------------------------------------------
-- 1. 价格系统分类表
-- ------------------------------------------------------
CREATE TABLE price_category
(
    id            BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    parent_id     BIGINT UNSIGNED COMMENT '父分类ID（顶级为NULL）',
    category_name VARCHAR(64)     NOT NULL COMMENT '分类名称',
    category_code VARCHAR(64)     NOT NULL COMMENT '分类唯一编码',
    level         INT             NOT NULL COMMENT '层级（1=一级分类，2=二级分类）',
    sort_order    INT             NOT NULL DEFAULT 0 COMMENT '排序值（越小越靠前）',
    status        TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=禁用',
    create_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_price_category_code (category_code),
    KEY idx_price_category_parent (parent_id),
    KEY idx_price_category_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '价格系统分类表';

-- ------------------------------------------------------
-- 2. 被定价物品表
-- ------------------------------------------------------
CREATE TABLE price_item
(
    id             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    item_code      VARCHAR(64)     NOT NULL COMMENT '物品唯一编码',
    item_name      VARCHAR(128)    NOT NULL COMMENT '物品名称',
    category_id    BIGINT UNSIGNED NOT NULL COMMENT '分类ID',
    unit           VARCHAR(32)     NOT NULL COMMENT '计价单位',
    specification  VARCHAR(128) COMMENT '规格描述',
    description    TEXT COMMENT '备注说明',
    ref_product_id BIGINT UNSIGNED COMMENT '关联商城商品ID',
    status         TINYINT(1)      NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=停用',
    create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_price_item_code (item_code),
    KEY idx_price_item_category (category_id),
    KEY idx_price_item_status (status),
    CONSTRAINT fk_price_item_category FOREIGN KEY (category_id)
        REFERENCES price_category (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '被定价物品表';

-- ------------------------------------------------------
-- 3. 价格来源表
-- ------------------------------------------------------
CREATE TABLE price_source
(
    id                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    source_name       VARCHAR(64)  NOT NULL COMMENT '来源名称',
    source_type       VARCHAR(32)  NOT NULL COMMENT '来源类型：API-接口，CRAWLER-爬虫，MANUAL-人工录入',
    source_url        VARCHAR(255) COMMENT '来源地址',
    reliability_level INT          NOT NULL DEFAULT 3 COMMENT '可靠等级1-5（5为最高）',
    remark            VARCHAR(255) COMMENT '备注',
    status            TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=禁用',
    create_time       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_price_source_status (status),
    KEY idx_price_source_type (source_type)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '价格来源表';

-- ------------------------------------------------------
-- 4. 地理位置表
-- ------------------------------------------------------
CREATE TABLE geo_location
(
    id            BIGINT UNSIGNED  AUTO_INCREMENT COMMENT '主键ID',
    location_name VARCHAR(128)     NOT NULL COMMENT '地点名称',
    location_type VARCHAR(32)      NOT NULL COMMENT '类型：COUNTRY-国家，CITY-城市，DISTRICT-区县，STORE-门店',
    parent_id     BIGINT UNSIGNED COMMENT '父级区域ID',
    longitude     DECIMAL(10, 7)   NOT NULL COMMENT '经度（GCJ-02坐标系）',
    latitude      DECIMAL(10, 7)   NOT NULL COMMENT '纬度（GCJ-02坐标系）',
    geo_hash      VARCHAR(12) COMMENT 'GeoHash编码',
    status        TINYINT(1)       NOT NULL DEFAULT 1 COMMENT '状态：1=启用，0=禁用',
    create_time   DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_geo_location_parent (parent_id),
    KEY idx_geo_location_geo (longitude, latitude)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '地理位置表';

-- ------------------------------------------------------
-- 5. 价格记录表（时间序列数据）
-- ------------------------------------------------------
CREATE TABLE price_record
(
    id             BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    item_id        BIGINT UNSIGNED NOT NULL COMMENT '物品ID',
    location_id    BIGINT UNSIGNED NOT NULL COMMENT '位置ID',
    price          DECIMAL(18, 6)  NOT NULL COMMENT '价格数值',
    currency       ENUM ('CNY', 'USD', 'EUR', 'JPY', 'GBP', 'HKD') NOT NULL DEFAULT 'CNY' COMMENT '币种',
    price_unit     VARCHAR(32)     NOT NULL COMMENT '价格单位',
    source_id      BIGINT UNSIGNED NOT NULL COMMENT '来源ID',
    effective_time DATETIME        NOT NULL COMMENT '生效时间',
    expire_time    DATETIME COMMENT '失效时间（NULL表示长期有效）',
    record_time    DATETIME        NOT NULL COMMENT '记录时间',
    confidence     DECIMAL(5, 2)   NOT NULL DEFAULT 100.00 COMMENT '可信度（0-100）',
    create_time    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_price_record_item (item_id),
    KEY idx_price_record_location (location_id),
    KEY idx_price_record_time (effective_time),
    KEY idx_price_record_source (source_id),
    CONSTRAINT fk_price_record_item FOREIGN KEY (item_id)
        REFERENCES price_item (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_price_record_location FOREIGN KEY (location_id)
        REFERENCES geo_location (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_price_record_source FOREIGN KEY (source_id)
        REFERENCES price_source (id) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '价格记录表（时间序列数据）';

-- ------------------------------------------------------
-- 6. 价格文件扩展表
-- ------------------------------------------------------
CREATE TABLE price_item_media
(
    id         BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    item_id    BIGINT UNSIGNED NOT NULL COMMENT '物品ID',
    media_type ENUM ('IMAGE', 'VIDEO', 'DOC') NOT NULL COMMENT '媒体类型',
    url        VARCHAR(255)    NOT NULL COMMENT 'URL',
    sort_order INT             NOT NULL DEFAULT 0 COMMENT '排序',
    create_time DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_price_item_media_item (item_id),
    CONSTRAINT fk_price_item_media_item FOREIGN KEY (item_id)
        REFERENCES price_item (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '价格文件扩展表';
