-- ======================================================
-- OWL user 表角色字段迁移：role_id -> role_name
-- 执行前建议先备份 user 表。
-- 只执行一次；新库直接使用 user_db.sql + user_init_data.sql。
-- ======================================================

-- 1. 确保角色数据存在
INSERT IGNORE INTO role (role_name, description, enabled, create_time, update_time)
VALUES ('USER', '平台普通注册用户，拥有基础浏览和购买权限', 1, NOW(), NOW()),
       ('MERCHANT', '入驻平台的商家用户，可发布商品、管理订单', 1, NOW(), NOW()),
       ('ADMIN', '系统管理员，拥有最高权限', 1, NOW(), NOW()),
       ('CUSTOMER_SERVICE', '平台客服人员，负责用户咨询和售后处理', 1, NOW(), NOW());

-- 2. 新增 role_name 列并回填
ALTER TABLE user
    ADD COLUMN role_name VARCHAR(100) NULL AFTER role_id,
    ADD KEY idx_role_name (role_name);

UPDATE user u
JOIN role r ON u.role_id = r.id
SET u.role_name = r.role_name;

-- 3. 校验回填结果：下面查询应为 0
-- SELECT COUNT(*) FROM user WHERE role_name IS NULL;

ALTER TABLE user
    MODIFY COLUMN role_name VARCHAR(100) NOT NULL;

-- 4. 删除旧的 role_id 外键、索引和列
ALTER TABLE user
    DROP FOREIGN KEY fk_user_role,
    DROP KEY idx_role_id,
    DROP COLUMN role_id;

-- 5. 建立新的 role_name 外键
ALTER TABLE user
    ADD CONSTRAINT fk_user_role
        FOREIGN KEY (role_name) REFERENCES role (role_name) ON DELETE RESTRICT;
