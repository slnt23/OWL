-- ======================================================
-- OWL 数据库：user 模块初始化数据
-- 模块：user
-- 说明：先执行 user_db.sql 建表，再执行本文件。
--       脚本可重复执行：角色按 role_name 去重，用户按唯一键去重。
-- 默认密码：
--   admin    / admin123
--   merchant / merchant123
--   user     / user123
--   service  / service123
-- ======================================================

-- ------------------------------------------------------
-- 1. 角色表初始化
-- ------------------------------------------------------
INSERT IGNORE INTO user_role (role_name, description, enabled, create_time, update_time)
VALUES ('USER', '平台普通注册用户，拥有基础浏览和购买权限', 1, NOW(), NOW()),
       ('MERCHANT', '入驻平台的商家用户，可发布商品、管理订单', 1, NOW(), NOW()),
       ('ADMIN', '系统管理员，拥有最高权限', 1, NOW(), NOW()),
       ('CUSTOMER_SERVICE', '平台客服人员，负责用户咨询和售后处理', 1, NOW(), NOW());

-- ------------------------------------------------------
-- 2. 用户表初始化
-- ------------------------------------------------------
INSERT IGNORE INTO user_account
(user_code, username, password, phone, email, avatar_url, nickname, remark, role_name, status, create_time, update_time)
VALUES
('U10001', 'admin', '$2a$10$tLCILQIBySPOoDkway5MXeeMrZL4YVJKDopPjqntSFuYkGeGO5I0y',
 '13800000001', 'admin@example.com', '/DEFAULT_AVATAR.png', '系统管理员', '初始化管理员账号', 'ADMIN', 0, NOW(), NOW()),
('U10002', 'merchant', '$2a$10$pIaHyCwE44gTfcPDhzephuKZ9/4aWgjrsz.GTBHnYwBrue1RNWBGm',
 '13800000002', 'merchant@example.com', '/DEFAULT_AVATAR.png', '示例商家', '初始化商家账号', 'MERCHANT', 0, NOW(), NOW()),
('U10003', 'user', '$2a$10$Bp09V58hSk58emMeVn4GleoEd5R/w/i8WVchyKuYB48VbN8zVDzMm',
 '13800000003', 'user@example.com', '/DEFAULT_AVATAR.png', '普通用户', '初始化普通用户', 'USER', 0, NOW(), NOW()),
('U10004', 'service', '$2a$10$ojjjF5.1Bk6hCa1FbEJomOTYF/kGFanRLsNbvSJ1Lz54hXdhgpupi',
 '13800000004', 'service@example.com', '/DEFAULT_AVATAR.png', '客服人员', '初始化客服账号', 'CUSTOMER_SERVICE', 0, NOW(), NOW());
