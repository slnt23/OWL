-- =============================================
-- 插入示例数据 - 按表依赖顺序执行
-- =============================================

-- 1. 用户表 (user)
INSERT INTO user (user_code, username, password, phone, email, nickname, avatar, role, status) VALUES
                                                                                                   ('U10001', 'admin01',   '$2a$10$hashedpassword123', '13800000001', 'admin@shop.com',    '系统管理员', 'https://example.com/avatars/admin.jpg', 2, 0),
                                                                                                   ('U10002', 'seller01',  '$2a$10$hashedpassword456', '13900000002', 'seller1@shop.com',  '优质水果店', 'https://example.com/avatars/seller1.jpg',  1, 0),
                                                                                                   ('U10003', 'seller02',  '$2a$10$hashedpassword789', '13600000003', 'seller2@shop.com',  '进口零食铺', 'https://example.com/avatars/seller2.jpg',  1, 0),
                                                                                                   ('U10004', 'user_jack', '$2a$10$hashedpasswordabc', '13500000004', 'jack123@qq.com',    'Jack陈',    NULL,                                     0, 0),
                                                                                                   ('U10005', 'user_lily', '$2a$10$hashedpassworddef', '13300000005', 'lily_sweet@163.com','小甜甜',    NULL,                                     0, 0);

-- 2. 角色表 (role)  ※ 如果您打算使用RBAC，可插入；否则可跳过
INSERT INTO role (role_name, description) VALUES
                                              ('ADMIN',   '系统管理员'),
                                              ('SELLER',  '商家'),
                                              ('USER',    '普通用户');

-- 3. 商品分类 (category) - 简单三层结构示例
INSERT INTO category (parent_id, name, level, sort, description) VALUES
                                                                     (0,  '生鲜水果',    1, 10, '新鲜水果蔬菜'),
                                                                     (0,  '休闲零食',    1, 20, '各种零食小吃'),
                                                                     (0,  '进口食品',    1, 30, '海外直采食品'),
                                                                     (1,  '热带水果',    2, 11, NULL),
                                                                     (1,  '国产水果',    2, 12, NULL),
                                                                     (2,  '坚果炒货',    2, 21, NULL),
                                                                     (2,  '糖果巧克力',  2, 22, NULL),
                                                                     (3,  '进口水果',    2, 31, NULL);

-- 4. 商品表 (product)
INSERT INTO product (category_id, seller_id, name, description, price, stock, status, cover_img) VALUES
                                                                                                     (4, 2, '越南进口红心火龙果', '果肉细腻多汁 甜度高',           18.80,  238, 0, 'https://img.example.com/fruits/dragonfruit.jpg'),
                                                                                                     (4, 2, '泰国金枕榴莲',       '浓香软糯 一口沦陷',               68.00,   45, 0, 'https://img.example.com/fruits/durian.jpg'),
                                                                                                     (5, 2, '陕西精品猕猴桃',     '果肉翠绿 酸甜适中',               12.90,  612, 0, 'https://img.example.com/fruits/kiwi.jpg'),
                                                                                                     (6, 3, '美国加州开心果',     '原味/盐焗 颗粒饱满',               59.90,  178, 0, 'https://img.example.com/nuts/pistachio.jpg'),
                                                                                                     (7, 3, '比利时进口黑巧克力', '72%黑巧 醇厚丝滑',                 38.50,   89, 0, 'https://img.example.com/choco/belgium_dark.jpg'),
                                                                                                     (8, 2, '智利车厘子JJ级',     '果径≥28mm 冰川车厘子',            128.00,  32, 0, 'https://img.example.com/fruits/cherry.jpg');

-- 5. 商品图片表 (product_image)
INSERT INTO product_image (product_id, image_url, sort) VALUES
                                                            (1, 'https://img.example.com/fruits/dragonfruit-1.jpg', 1),
                                                            (1, 'https://img.example.com/fruits/dragonfruit-2.jpg', 2),
                                                            (1, 'https://img.example.com/fruits/dragonfruit-cut.jpg', 3),

                                                            (2, 'https://img.example.com/fruits/durian-whole.jpg', 1),
                                                            (2, 'https://img.example.com/fruits/durian-flesh.jpg', 2),

                                                            (4, 'https://img.example.com/nuts/pistachio-pack.jpg', 1),
                                                            (4, 'https://img.example.com/nuts/pistachio-closeup.jpg', 2),

                                                            (6, 'https://img.example.com/fruits/cherry-box.jpg', 1),
                                                            (6, 'https://img.example.com/fruits/cherry-single.jpg', 2),
                                                            (6, 'https://img.example.com/fruits/cherry-detail.jpg', 3);

-- 6. 收货地址 (user_address)
INSERT INTO user_address (user_id, receiver_name, receiver_phone, province, city, district, detail, is_default) VALUES
                                                                                                                    (4, '陈先生', '13500000004', '浙江省', '杭州市', '西湖区', '文三路478号华星时代广场A座1803', 1),
                                                                                                                    (4, '陈先生', '13500000004', '广东省', '广州市', '天河区', '珠江新城花城大道东85号高德置地广场', 0),
                                                                                                                    (5, '李丽',   '13300000005', '上海市', '浦东新区', '陆家嘴', '银城中路68号时代金融中心9楼', 1);

-- 7. 购物车示例 (cart_item)
INSERT INTO cart_item (user_id, product_id, quantity, checked) VALUES
                                                                   (4, 1, 2, 1),   -- Jack 加了2个火龙果
                                                                   (4, 4, 1, 1),   -- 加了1袋开心果
                                                                   (4, 6, 3, 0),   -- 加了3斤车厘子 但没勾选
                                                                   (5, 2, 1, 1),   -- 小甜甜 加了1个榴莲
                                                                   (5, 5, 2, 1);   -- 加了2块黑巧

-- 8. 收藏示例 (favorite)
INSERT INTO favorite (user_id, product_id) VALUES
                                               (4, 1), (4, 6), (4, 4),    -- Jack 收藏了火龙果、车厘子、开心果
                                               (5, 2), (5, 6);            -- 小甜甜 收藏了榴莲和车厘子

-- 9. 浏览记录示例 (view_history) - 按时间倒序插入模拟浏览轨迹
INSERT INTO view_history (user_id, product_id, view_time) VALUES
                                                              (4, 6, '2025-10-20 14:35:22'),
                                                              (4, 4, '2025-10-20 14:32:10'),
                                                              (4, 1, '2025-10-20 14:28:45'),
                                                              (5, 2, '2025-10-19 21:15:33'),
                                                              (5, 5, '2025-10-19 21:10:12'),
                                                              (5, 6, '2025-10-19 20:58:47');


-- 订单主表示例数据
INSERT INTO order_mast (order_no, user_id, total_amount, pay_status, order_status, pay_time, delivery_time, finish_time, address_snapshot)
VALUES
-- 订单1：已完成的订单
('ORD20260117001', 1001, 299.80, 1, 3, '2026-01-17 10:05:30', '2026-01-17 14:20:15', '2026-01-18 09:15:40', '{"province":"广东省","city":"深圳市","district":"南山区","detail":"科技园路88号","receiver":"张三","phone":"13800138000"}'),
-- 订单2：待发货的订单
('ORD20260117002', 1002, 1599.00, 1, 1, '2026-01-17 11:20:00', NULL, NULL, '{"province":"浙江省","city":"杭州市","district":"西湖区","detail":"文三路45号","receiver":"李四","phone":"13900139000"}'),
-- 订单3：待支付的订单
('ORD20260117003', 1001, 89.90, 0, 0, NULL, NULL, NULL, '{"province":"北京市","city":"北京市","district":"朝阳区","detail":"建国路88号","receiver":"张三","phone":"13800138000"}');

-- 订单明细示例数据（关联上述3个订单）
INSERT INTO order_detail (order_id, product_id, product_name, product_image, unit_price, quantity, remark, total_price)
VALUES
-- 订单1的明细（2个商品）
(1, 5001, '无线鼠标', 'https://img.example.com/5001.jpg', 99.90, 2, '静音款', 199.80),
(1, 5002, '键盘保护膜', 'https://img.example.com/5002.jpg', 50.00, 2, '通用尺寸', 100.00),
-- 订单2的明细（1个商品）
(2, 6001, '笔记本电脑', 'https://img.example.com/6001.jpg', 1599.00, 1, '16G+512G版本', 1599.00),
-- 订单3的明细（1个商品）
(3, 7001, '手机支架', 'https://img.example.com/7001.jpg', 89.90, 1, '可折叠', 89.90);