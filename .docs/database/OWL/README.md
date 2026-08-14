# OWL 数据库目录

本目录维护 OWL 数据库的 canonical DDL。`*_db.sql` 是当前模块最新建表语句，历史版本放在 `archive/`，示例数据放在对应模块目录或 `seed/`。

## 当前规范

- 数据库：`OWL`，字符集 `utf8mb4`，排序规则 `utf8mb4_0900_ai_ci`。
- 表名：小写 snake_case、单数，不使用保留字。
- 字段名：小写 snake_case，Java 属性使用 camelCase。
- 反引号：不用于普通标识符；仅当字段是 MySQL 保留字或包含特殊字符时必须使用，优先通过改名避免。
- 时间字段：统一为 `create_time` / `update_time`。
- 主键：`BIGINT UNSIGNED AUTO_INCREMENT`；UUID 场景使用 `CHAR(36)`。
- 索引：`idx_<table>_<column>`，唯一索引 `uk_<table>_<column>`，外键 `fk_<table>_<column>`。
- canonical DDL 只包含 `CREATE TABLE`，历史 `ALTER` 已合并进建表语句。

## 执行顺序

1. `init.sql`
2. `user/user_db.sql`
3. `log/log_db.sql`
4. `admin/admin_db.sql`
5. `crow/crow_db.sql`
6. `sugarcane/sugarcane_db.sql`

## 模块说明

- `user`：`role`、`user`、`user_address`。
- `log`：`user_log`、`biz_log`、`admin_log`。
- `admin`：`spotlight`、`feature`。
- `crow`：`conversation`、`message`。
- `sugarcane`：`price_category`、`price_item`、`price_source`、`geo_location`、`price_record`、`price_item_media`。

`pitaya` 模块已暂停开发，其数据库脚本和模块代码保持现状，本次不纳入规范。
