-- ======================================================
-- 用户模块 v4 迁移脚本
-- 1. user.email 增加唯一索引
-- 2. user_address 增加 update_time
-- 执行前请先确认 user.email 无重复数据
-- ======================================================

ALTER TABLE user
    ADD UNIQUE KEY uk_email (email);

ALTER TABLE user_address
    ADD COLUMN update_time DATETIME DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间';
