-- ======================================================
-- OWL 数据库：crow 模块 canonical DDL
-- 模块：crow
-- MySQL 版本：8.4
-- ======================================================

-- ------------------------------------------------------
-- 1. 对话会话表
-- ------------------------------------------------------
CREATE TABLE conversation
(
    id           CHAR(36)     NOT NULL COMMENT '会话ID（UUID，主键）',
    user_code    VARCHAR(64)  NOT NULL COMMENT '用户账号编号',
    title        VARCHAR(255) COMMENT '对话标题',
    total_tokens INT          NOT NULL DEFAULT 0 COMMENT '累计token消耗',
    deleted      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否删除：0=正常，1=已删除',
    create_time  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',
    update_time  DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_conversation_user_code (user_code),
    KEY idx_conversation_create_time (create_time),
    KEY idx_conversation_deleted (deleted)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = 'AI对话会话表';

-- ------------------------------------------------------
-- 2. 对话消息表
-- ------------------------------------------------------
CREATE TABLE message
(
    id                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '消息主键',
    conversation_id   CHAR(36)     NOT NULL COMMENT '所属会话ID',
    role              VARCHAR(20)  NOT NULL COMMENT '消息角色：user / assistant / system',
    content           TEXT         NOT NULL COMMENT '消息内容',
    prompt_tokens     INT COMMENT '输入token',
    completion_tokens INT COMMENT '输出token',
    total_token_count INT COMMENT '该消息总token数',
    model             VARCHAR(50) COMMENT '使用的模型名称',
    finish_reason     VARCHAR(30) COMMENT '生成终止原因',
    create_time       DATETIME(3)  NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间',

    PRIMARY KEY (id),
    KEY idx_message_conversation_id (conversation_id),
    KEY idx_message_create_time (create_time),
    KEY idx_message_model (model),
    KEY idx_message_finish_reason (finish_reason),
    CONSTRAINT fk_message_conversation FOREIGN KEY (conversation_id)
        REFERENCES conversation (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = 'AI对话消息表';
