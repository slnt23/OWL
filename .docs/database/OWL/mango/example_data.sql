-- ======================================================
-- OWL 数据库：mango 模块示例数据
-- 模块：mango（个人博客）
-- 前置条件：已执行 user_db.sql 与 user_init_data.sql（依赖 admin 用户）
-- 说明：仅用于本地演示，正式环境按站长实际信息修改或删除。
-- ======================================================

-- ------------------------------------------------------
-- 站长个人信息（单行，id 固定为 1）
-- ------------------------------------------------------
INSERT INTO blog_profile (id, avatar_url, name, tagline, bio, location, github_url, website_url, email, codetime_uid)
VALUES (1,
        '/images/avatar/owl.png',
        'OWL 站长',
        'Developer / Designer',
        '个人全栈项目 OWL 的维护者，关注后端架构与操作系统。',
        '杭州',
        'https://github.com/example',
        'https://example.com',
        'owner@example.com',
        'demo-uid-001');

-- ------------------------------------------------------
-- 教育经历
-- ------------------------------------------------------
INSERT INTO blog_education (school, degree, period, sort_order)
VALUES ('示例大学', '计算机科学与技术 学士', '2020.09 - 2024.06', 1),
       ('示例高中', '理科实验班', '2017.09 - 2020.06', 2);

-- ------------------------------------------------------
-- 技能分类与条目
-- ------------------------------------------------------
INSERT INTO blog_skill_category (category, sort_order)
VALUES ('后端', 1),
       ('前端', 2),
       ('工具链', 3);

INSERT INTO blog_skill_item (category_id, name, sort_order)
VALUES ((SELECT id FROM blog_skill_category WHERE category = '后端'), 'Java', 1),
       ((SELECT id FROM blog_skill_category WHERE category = '后端'), 'Spring Boot', 2),
       ((SELECT id FROM blog_skill_category WHERE category = '后端'), 'MySQL', 3),
       ((SELECT id FROM blog_skill_category WHERE category = '前端'), 'Vue 3', 1),
       ((SELECT id FROM blog_skill_category WHERE category = '前端'), 'TypeScript', 2),
       ((SELECT id FROM blog_skill_category WHERE category = '工具链'), 'Git', 1),
       ((SELECT id FROM blog_skill_category WHERE category = '工具链'), 'Docker', 2);

-- ------------------------------------------------------
-- 文章分类
-- ------------------------------------------------------
INSERT INTO blog_category (name, slug, sort_order)
VALUES ('操作系统', 'os', 1),
       ('后端开发', 'backend', 2),
       ('随笔', 'notes', 3);

-- ------------------------------------------------------
-- 标签
-- ------------------------------------------------------
INSERT INTO blog_tag (name, slug)
VALUES ('xv6', 'xv6'),
       ('Java', 'java'),
       ('Spring', 'spring'),
       ('数据库', 'database'),
       ('随笔', 'notes');

-- ------------------------------------------------------
-- 文章（created_by 关联 admin 用户）
-- ------------------------------------------------------
INSERT INTO blog_post (title, excerpt, content, cover_image, slug, category_id, lang, read_time, is_published, is_top, view_count, like_count, publish_time, created_by)
VALUES ('xv6 操作系统实验（八）',
        '本系列记录 xv6 OS 实验的最后一篇：文件系统与启动流程梳理。',
        '# xv6 实验（八）\n\n本文梳理 xv6 文件系统的整体结构……',
        '/images/posts/xv6-os-lab-part8.jpg',
        'xv6-os-lab-part8',
        (SELECT id FROM blog_category WHERE slug = 'os'),
        'zh',
        15,
        1,
        1,
        128,
        12,
        '2026-08-10 09:30:00',
        (SELECT id FROM user WHERE user_code = 'admin')),
       ('OWL 后端模块架构演进',
        '从单体到多模块：OWL 后端按业务拆分子模块的实践与取舍。',
        '# OWL 后端模块架构\n\n本文记录 OWL 后端从单体演进到多模块的过程……',
        '/images/posts/owl-backend-architecture.jpg',
        'owl-backend-module-architecture',
        (SELECT id FROM blog_category WHERE slug = 'backend'),
        'zh',
        10,
        1,
        0,
        56,
        5,
        '2026-08-18 14:00:00',
        (SELECT id FROM user WHERE user_code = 'admin')),
       ('一篇草稿（未发布）',
        '这篇文章还在写作中，不会在列表出现。',
        '# 草稿\n\n内容待完善……',
        NULL,
        'draft-post',
        (SELECT id FROM blog_category WHERE slug = 'notes'),
        'zh',
        3,
        0,
        0,
        0,
        0,
        NULL,
        (SELECT id FROM user WHERE user_code = 'admin'));

-- ------------------------------------------------------
-- 文章-标签关联
-- ------------------------------------------------------
INSERT INTO blog_post_tag (post_id, tag_id)
VALUES ((SELECT id FROM blog_post WHERE slug = 'xv6-os-lab-part8'),
        (SELECT id FROM blog_tag WHERE slug = 'xv6')),
       ((SELECT id FROM blog_post WHERE slug = 'owl-backend-module-architecture'),
        (SELECT id FROM blog_tag WHERE slug = 'java')),
       ((SELECT id FROM blog_post WHERE slug = 'owl-backend-module-architecture'),
        (SELECT id FROM blog_tag WHERE slug = 'spring')),
       ((SELECT id FROM blog_post WHERE slug = 'draft-post'),
        (SELECT id FROM blog_tag WHERE slug = 'notes'));
