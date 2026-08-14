-- 一级分类
INSERT INTO category (parent_id, name, level, sort, description, enabled)
VALUES
    (0, '数码产品', 1, 1, '手机、电脑、耳机等数码类商品', 1),
    (0, '家居用品', 1, 2, '家具、厨具、家纺等居家用品', 1),
    (0, '服饰鞋帽', 1, 3, '衣服、鞋子、帽子等穿戴类商品', 1),
-- 二级分类（归属数码产品）
    (1, '智能手机', 2, 1, '各品牌智能手机', 1),
    (1, '笔记本电脑', 2, 2, '轻薄本、游戏本等笔记本', 1),
    (1, '蓝牙耳机', 2, 3, '无线蓝牙耳机', 1),
-- 二级分类（归属家居用品）
    (2, '厨房厨具', 2, 1, '锅碗瓢盆、刀具等厨具', 1),
    (2, '床上用品', 2, 2, '被子、枕头、床单等', 1),
-- 三级分类（归属智能手机）
    (4, '苹果手机', 3, 1, 'iPhone系列手机', 1),
    (4, '华为手机', 3, 2, '华为Mate/P系列等', 1);


INSERT INTO product (category_id, seller_id, name, description, price, stock, status, cover_img)
VALUES
-- 数码产品-智能手机-苹果
(9, 1001, 'iPhone 16 Pro', '新款苹果手机，搭载A18芯片，支持5G', 7999.00, 500, 0, 'https://img.example.com/iphone16pro.jpg'),
(9, 1001, 'iPhone 15', '苹果15系列，6.1英寸屏幕', 5999.00, 800, 0, 'https://img.example.com/iphone15.jpg'),
-- 数码产品-智能手机-华为
(10, 1002, '华为Mate 70 Pro', '鸿蒙4.0系统，麒麟芯片', 6999.00, 300, 0, 'https://img.example.com/mate70pro.jpg'),
(10, 1002, '华为Pura 80', '轻薄拍照手机，超感光主摄', 4999.00, 600, 0, 'https://img.example.com/pura80.jpg'),
-- 数码产品-笔记本电脑
(5, 1003, '联想小新Pro 16', '锐龙7处理器，2.5K高刷屏', 4599.00, 200, 0, 'https://img.example.com/xiaoxinpro16.jpg'),
(5, 1003, '苹果MacBook Air M3', 'M3芯片，续航18小时', 8999.00, 150, 0, 'https://img.example.com/macbookairm3.jpg'),
-- 数码产品-蓝牙耳机
(6, 1004, 'AirPods Pro 2', '主动降噪，空间音频', 1799.00, 1000, 0, 'https://img.example.com/airpodspro2.jpg'),
(6, 1004, '华为FreeBuds Pro 3', '星闪连接，降噪深度45dB', 1299.00, 800, 0, 'https://img.example.com/freebudspro3.jpg'),
-- 家居用品-厨房厨具
(7, 1005, '苏泊尔不粘锅', '麦饭石涂层，不粘易清洗', 199.00, 2000, 0, 'https://img.example.com/supor-pan.jpg'),
(7, 1005, '双立人刀具套装', '不锈钢材质，6件套', 899.00, 300, 0, 'https://img.example.com/zwilling-knife.jpg'),
-- 家居用品-床上用品
(8, 1006, '水星家纺纯棉四件套', '100%纯棉，亲肤透气', 299.00, 1500, 0, 'https://img.example.com/shuixing-bed.jpg'),
(8, 1006, '南极人羽绒被', '90%白鸭绒，保暖轻便', 499.00, 800, 1, 'https://img.example.com/nanjiren-quilt.jpg'); -- 下架状态


INSERT INTO product_image (product_id, image_url, sort)
VALUES
-- iPhone 16 Pro 图片（product_id=1）
(1, 'https://img.example.com/iphone16pro-1.jpg', 1),
(1, 'https://img.example.com/iphone16pro-2.jpg', 2),
(1, 'https://img.example.com/iphone16pro-3.jpg', 3),
-- iPhone 15 图片（product_id=2）
(2, 'https://img.example.com/iphone15-1.jpg', 1),
(2, 'https://img.example.com/iphone15-2.jpg', 2),
-- 华为Mate 70 Pro 图片（product_id=3）
(3, 'https://img.example.com/mate70pro-1.jpg', 1),
(3, 'https://img.example.com/mate70pro-2.jpg', 2),
(3, 'https://img.example.com/mate70pro-3.jpg', 3),
(3, 'https://img.example.com/mate70pro-4.jpg', 4),
-- 联想小新Pro 16 图片（product_id=6）
(6, 'https://img.example.com/xiaoxinpro16-1.jpg', 1),
(6, 'https://img.example.com/xiaoxinpro16-2.jpg', 2),
-- 苏泊尔不粘锅 图片（product_id=10）
(10, 'https://img.example.com/supor-pan-1.jpg', 1),
(10, 'https://img.example.com/supor-pan-2.jpg', 2),
-- 水星家纺四件套 图片（product_id=11）
(11, 'https://img.example.com/shuixing-bed-1.jpg', 1),
(11, 'https://img.example.com/shuixing-bed-2.jpg', 2);