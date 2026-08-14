-- ============================================
-- 首页焦点展示项目表
-- 用于管理首页轮播、推荐位等焦点内容


#     这个删除不用


-- ============================================
CREATE TABLE spotlight
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID，唯一标识',
    eyebrow     VARCHAR(100) NOT NULL COMMENT '眉题/前置标题，通常为短标签或引导文字',
    title       VARCHAR(200) NOT NULL COMMENT '主标题，焦点项目的核心文案',
    description TEXT COMMENT '详细描述，可包含HTML格式或纯文本',
    image_url       VARCHAR(500) NOT NULL COMMENT '配图，存储相对路径或CDN完整URL',
    `order`     INT         DEFAULT 0 COMMENT '排序序号，数学越小越靠前；同数值按创建时间排序',
    link        VARCHAR(500) COMMENT '点击跳转链接，可为内部路由或外部URL',
    target      VARCHAR(20) DEFAULT '_self' COMMENT '链接打开方式：_self当前页、_blank新标签页、_parent父框架',
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，行首次插入时自动设置',
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间，行每次修改时自动刷新',
    -- 为排序查询创建索引，后端列表接口通常按 order 排序
    INDEX idx_order (`order`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='首页焦点展示项目表';

# alter table spotlight change column imageUrl image_url varchar(500);


-- ============================================
-- 产品特性展示表
-- 用于展示产品的核心卖点、功能亮点
-- ============================================
CREATE TABLE feature
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID，唯一标识每条特性',
    icon        VARCHAR(500) NOT NULL COMMENT '图标，可为Font Awesome类名、SVG内容或图片地址',
    title       VARCHAR(200) NOT NULL COMMENT '特性标题，概括功能或卖点的简短文案',
    description TEXT COMMENT '特性详细说明，支持纯文本或Markdown格式',
    -- 新增：为特性项增加排序能力，前端展示顺序由该字段控制
    sort_order  INT       DEFAULT 0 COMMENT '排序序号，数值越小越靠前；用于控制特性展示顺序',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录最后更新时间'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='产品特性展示表';
