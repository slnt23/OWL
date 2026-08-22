-- ======================================================
-- OWL 数据库：caishen 模块 canonical DDL
-- 模块：caishen（理财中心，代号 caishen）
-- MySQL 版本：8.4
-- 说明：V1 基金监控与提醒。基金档案 / 净值历史 / 用户关注 /
--       提醒规则 / 区间总结。caishen_fund_alert.watch_id 级联删除。
--       caishen_fund_nav 为追加型历史表，无 update_time。
-- ======================================================

-- ------------------------------------------------------
-- 1. 基金档案表
-- ------------------------------------------------------
CREATE TABLE caishen_fund
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    fund_code   VARCHAR(20)  NOT NULL COMMENT '基金代码，如 000001',
    fund_name   VARCHAR(100) NOT NULL COMMENT '基金名称',
    fund_type   VARCHAR(20) COMMENT '基金类型：股票型/混合型/债券型/货币型/指数型',
    status      TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '状态：1=正常，0=停用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_caishen_fund_fund_code (fund_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '基金档案表';

-- ------------------------------------------------------
-- 2. 基金净值历史表（追加型，无 update_time）
-- ------------------------------------------------------
CREATE TABLE caishen_fund_nav
(
    id                BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    fund_code         VARCHAR(20)   NOT NULL COMMENT '基金代码',
    nav_date          DATE          NOT NULL COMMENT '净值日期',
    unit_nav          DECIMAL(10,4) NOT NULL COMMENT '单位净值',
    accumulated_nav   DECIMAL(10,4) COMMENT '累计净值',
    daily_return_rate DECIMAL(8,4) COMMENT '日收益率（%），如 1.2345 表示 1.2345%',
    source            VARCHAR(20) COMMENT '数据来源：mock/python/manual',
    create_time       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_caishen_fund_nav_fund_nav_date (fund_code, nav_date),
    KEY idx_caishen_fund_nav_nav_date (nav_date)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '基金净值历史表';

-- ------------------------------------------------------
-- 3. 用户关注基金表
-- ------------------------------------------------------
CREATE TABLE caishen_fund_watch
(
    id          BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id     BIGINT      NOT NULL COMMENT '用户ID，关联 user.id',
    fund_code   VARCHAR(20) NOT NULL COMMENT '基金代码',
    remark      VARCHAR(200) COMMENT '用户备注，如"定投基金"',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    UNIQUE KEY uk_caishen_fund_watch_user_fund (user_id, fund_code),
    KEY idx_caishen_fund_watch_user_id (user_id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户关注基金表';

-- ------------------------------------------------------
-- 4. 基金提醒规则表
-- ------------------------------------------------------
CREATE TABLE caishen_fund_alert
(
    id                 BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    watch_id           BIGINT       NOT NULL COMMENT '关联 caishen_fund_watch.id，级联删除',
    alert_type         VARCHAR(20)  NOT NULL COMMENT '提醒类型：RISE_ABOVE（涨到）/ FALL_BELOW（跌到）',
    threshold_value    DECIMAL(10,4) COMMENT '绝对净值阈值',
    threshold_percent  DECIMAL(8,4) COMMENT '涨跌幅阈值（%），如 2.0000 表示 2%',
    status             VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE=生效中, TRIGGERED=已触发, PAUSED=暂停',
    last_triggered_at  DATETIME COMMENT '最近一次触发时间',
    create_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_caishen_fund_alert_watch_id (watch_id),
    KEY idx_caishen_fund_alert_status (status),
    CONSTRAINT fk_caishen_fund_alert_watch FOREIGN KEY (watch_id)
        REFERENCES caishen_fund_watch (id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '基金提醒规则表';

-- ------------------------------------------------------
-- 5. 用户总结记录表
-- ------------------------------------------------------
CREATE TABLE caishen_summary
(
    id              BIGINT UNSIGNED AUTO_INCREMENT COMMENT '主键ID',
    user_id         BIGINT      NOT NULL COMMENT '用户ID，关联 user.id',
    period_type     VARCHAR(10) NOT NULL COMMENT '周期类型：DAILY/WEEKLY/MONTHLY',
    start_date      DATE        NOT NULL COMMENT '统计开始日期',
    end_date        DATE        NOT NULL COMMENT '统计结束日期',
    metric_snapshot TEXT COMMENT '指标快照 JSON，如各基金涨跌幅',
    summary_text    TEXT COMMENT 'AI 生成的总结文本',
    status          VARCHAR(20) NOT NULL COMMENT '状态：PENDING/PROCESSING/SUCCESS/FAILED',
    error_message   VARCHAR(500) COMMENT '失败时的错误信息',
    create_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    PRIMARY KEY (id),
    KEY idx_caishen_summary_user_id (user_id),
    KEY idx_caishen_summary_status (status)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户总结记录表';
