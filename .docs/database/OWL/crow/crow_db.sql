-- =============================================================
-- OWL 项目 - crow AI 模块 - 第一版数据库表结构
-- 表名：conversation（对话会话表）与 message（对话消息表）
-- 创建日期：2026-04
-- =============================================================

-- 1. Conversation 表（对话会话表）
CREATE TABLE IF NOT EXISTS `conversation`
(
    `id`           CHAR(36)     NOT NULL COMMENT '会话ID（UUID，主键）',
    `user_code`    VARCHAR(64)  NOT NULL COMMENT '用户code_ID（支持多用户）',
    `title`        VARCHAR(255) NULL COMMENT '对话标题（可由AI自动生成或用户手动修改）', -- 新增字段
    `total_tokens` INT          NULL DEFAULT 0 COMMENT '累计token消耗',
    `deleted`      TINYINT      NULL DEFAULT 0 COMMENT '是否删除（软删除：0=正常，1=已删除）',
    `created_at`   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '会话创建时间',
    `updated_at`   DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '会话最后更新时间',

    PRIMARY KEY (`id`),
    INDEX `idx_conversation_user_id` (`user_code`),
    INDEX `idx_conversation_created_at` (`created_at`),
    INDEX `idx_conversation_deleted` (`deleted`)                                       -- 建议新增，用于软删除查询优化
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='AI对话会话表 - 用于管理用户与AI的对话会话';

-- 2. Message 表（对话消息表）
CREATE TABLE IF NOT EXISTS `message`
(
    `id`                BIGINT      NOT NULL AUTO_INCREMENT COMMENT '消息主键（自增）',
    `conversation_id`   CHAR(36)    NOT NULL COMMENT '所属会话ID（外键关联 conversation）',
    `role`              VARCHAR(20) NOT NULL COMMENT '消息角色：user / assistant / system',
    `content`           TEXT        NOT NULL COMMENT '消息具体内容', -- 新增字段
    `prompt_tokens`     INT         null comment '输入token',
    `completion_tokens` INT         NULL COMMENT '输出token',
    `total_token_count` INT         NULL COMMENT '该消息总token数',
    `model`             VARCHAR(50) NULL COMMENT '使用的模型名称',
    `finish_reason`     VARCHAR(30) NULL COMMENT '生成终止原因（stop / length / content_filter 等）',
    `created_at`        DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '消息创建时间',

    PRIMARY KEY (`id`),
    INDEX `idx_message_conversation_id` (`conversation_id`),
    INDEX `idx_message_created_at` (`created_at`),-- 建议新增索引（根据实际查询场景可进一步优化）
    INDEX `idx_message_model` (`model`),
    INDEX `idx_message_finish_reason` (`finish_reason`),

    CONSTRAINT `fk_message_conversation`
        FOREIGN KEY (`conversation_id`)
            REFERENCES `conversation` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='AI对话消息表 - 存储每轮对话的具体内容';


