-- 角色表示例数据（role_name 为英文）
INSERT INTO `role` (`role_name`, `description`, `enabled`, `create_time`)
VALUES ('USER', '平台普通注册用户，拥有基础浏览和购买权限', 1, NOW()),
       ('MERCHANT', '入驻平台的商家用户，可发布商品、管理订单', 1, NOW()),
       ('ADMIN', '系统管理员，拥有最高权限', 1, NOW()),
       ('CUSTOMER_SERVICE', '平台客服人员，负责用户咨询和售后处理', 1, NOW());
