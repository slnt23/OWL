-- 插入价格分类示例数据（支持层级）
INSERT INTO price_category (parent_id, category_name, category_code, level, sort_order, status)
VALUES (NULL, '食品饮料', 'FOOD', 1, 10, 1),
       (NULL, '能源燃料', 'ENERGY', 1, 20, 1),
       (NULL, '建筑材料', 'BUILDING', 1, 30, 1),
       (NULL, '农产品', 'AGRI', 1, 40, 1),

-- 二级分类
       (1, '食用油', 'FOOD_OIL', 2, 10, 1),
       (1, '牛奶乳制品', 'FOOD_MILK', 2, 20, 1),
       (4, '谷物', 'AGRI_GRAIN', 2, 10, 1),
       (4, '蔬菜', 'AGRI_VEG', 2, 20, 1),
       (2, '成品油', 'ENERGY_OIL', 2, 10, 1);


INSERT INTO price_item (item_code, item_name, category_id, unit, specification, description, status)
VALUES ('OIL001', '花生油', 5, '升', '5L桶装', '一级压榨花生油', 1),
       ('OIL002', '大豆油', 5, '升', '5L桶装', '非转基因大豆油', 1),
       ('MILK001', '纯牛奶', 6, '升', '1L盒装', '全脂纯牛奶', 1),
       ('RICE001', '东北大米', 8, '公斤', '5kg袋装', '2025年新米', 1),
       ('GAS001', '92号汽油', 10, '升', '', '国VI标准92号汽油', 1),
       ('CEMENT001', '普通硅酸盐水泥', 3, '吨', 'P.O 42.5', '散装水泥', 1);


INSERT INTO price_source (source_name, source_type, source_url, reliability_level, remark, status)
VALUES ('国家统计局', 'API', 'https://data.stats.gov.cn', 5, '官方数据，最高可信度', 1),
       ('商务部价格监测', 'API', 'http://price.mofcom.gov.cn', 5, '全国重要商品价格监测', 1),
       ('北京新发地批发市场', 'CRAWLER', '', 4, '大型农产品批发市场', 1),
       ('上海石油交易所', 'API', '', 4, '成品油价格参考', 1),
       ('人工录入-市场调研', 'MANUAL', '', 3, '实地调研数据', 1);


INSERT INTO geo_location (location_name, location_type, parent_id, longitude, latitude, geo_hash, status)
VALUES ('中国', 'COUNTRY', NULL, 104.195397, 35.861660, 'ww8', 1),
       ('北京市', 'CITY', 1, 116.407394, 39.904211, 'wx4g', 1),
       ('上海市', 'CITY', 1, 121.473701, 31.230416, 'wtw3', 1),
       ('朝阳区', 'DISTRICT', 2, 116.486419, 39.921489, 'wx4g8', 1),
       ('新发地批发市场', 'STORE', 4, 116.332000, 39.850000, 'wx4g7', 1);


INSERT INTO price_record
(item_id, location_id, price, currency, price_unit, source_id, effective_time, expire_time, record_time, confidence)
VALUES
-- 花生油 北京
(1, 4, 68.50, 'CNY', '元/升', 2, '2026-04-01 00:00:00', '2026-04-30 23:59:59', '2026-04-28 08:00:00', 95.00),

-- 大豆油 上海
(2, 3, 62.80, 'CNY', '元/升', 2, '2026-04-01 00:00:00', NULL, '2026-04-27 14:30:00', 92.00),

-- 纯牛奶 北京
(3, 4, 12.90, 'CNY', '元/升', 1, '2026-04-20 00:00:00', '2026-05-20 23:59:59', '2026-04-28 09:15:00', 98.00),

-- 东北大米 北京新发地
(4, 5, 5.68, 'CNY', '元/公斤', 3, '2026-04-25 00:00:00', NULL, '2026-04-28 10:00:00', 88.00),

-- 92号汽油 北京
(5, 2, 7.85, 'CNY', '元/升', 4, '2026-04-28 00:00:00', NULL, '2026-04-28 00:30:00', 99.00);


INSERT INTO price_item_media (item_id, media_type, url, sort_order)
VALUES (1, 'IMAGE', 'https://example.com/media/oil_peanut_01.jpg', 10),
       (1, 'IMAGE', 'https://example.com/media/oil_peanut_label.jpg', 20),
       (3, 'IMAGE', 'https://example.com/media/milk_pure_01.jpg', 10),
       (4, 'IMAGE', 'https://example.com/media/rice_ne.jpg', 10);