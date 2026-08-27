# OWL 数据库命名规范

| 属性     | 值         |
| -------- | ---------- |
| 状态     | 已批准     |
| 负责人   | 仓库 Owner |
| 创建时间 | 2026-08-25 |
| 更新时间 | 2026-08-27 |
| 适用版本 | 全部       |

## 背景与目标

对于**大型项目、多模块系统、微服务架构**来说，数据库表命名是否加前缀/后缀，没有绝对答案，但业界有比较成熟的实践：

> **不要为了"看起来区分"而给所有表乱加前缀；应该根据数据库边界、模块边界、生命周期来决定。**

本文档约束 OWL 仓库中所有数据库表名和字段名的命名规则，目标是让表名一眼可知归属模块、一眼可知业务含义，字段名自解释、无歧义。

---

# 第一部分：表命名

## 核心原则

优先使用**领域/模块前缀**，不使用无意义后缀；不加项目名前缀，除非多个项目共享数据库。

比较成熟的风格：

```
数据库:
ai_platform

表:
agent_info
agent_skill
dataset_info
label_task
user_account
```

而不是：

```
ai_platform_agent_info
tbl_agent_info
agent_info_table
```

## 场景分类

### 1. 单项目单数据库：推荐「模块前缀 + 业务名」

OWL 当前属于此场景。例如一个大型 SaaS 平台：

```
用户中心
订单中心
支付中心
数据标注中心
```

数据库：

```
user_xxx
order_xxx
payment_xxx
label_xxx
```

例如：

```
user_account
user_role
user_permission

order_info
order_item
order_payment

label_task
label_dataset
label_annotation
```

#### 优点

一眼知道归属模块。比如看到：

```
label_annotation
```

马上知道：

> 这是数据标注模块的标注记录

而不是：

```
annotation
```

需要查表结构才知道。

### 2. 不推荐所有表都用项目名前缀

例如项目叫 `cloudlisten`，然后：

```
cloudlisten_user
cloudlisten_order
cloudlisten_message
cloudlisten_task
```

通常没有必要。

#### 原因一：数据库本身已经代表项目

```
cloudlisten_db

    user_account
    order_info
    message
```

已经很清晰。否则：

```
cloudlisten_user_account
cloudlisten_order_info
```

属于重复信息。

#### 原因二：未来拆分困难

比如以后：

```
user模块 -> 独立用户中心服务
```

表名：

```
cloudlisten_user
```

反而成为历史包袱。

### 3. 多项目共用一个数据库：需要项目级前缀

比如公司：

```
数据库:
company_prod
```

里面有：

项目A:

```
erp_user
erp_order
erp_invoice
```

项目B:

```
crm_user
crm_customer
crm_contact
```

项目C:

```
ai_task
ai_model
ai_dataset
```

这种情况必须区分。否则：

```
user
user
user
```

直接冲突。

### 4. 微服务架构：更推荐「一个服务一个数据库」

例如：

```
user-service

 user_account
 user_role


order-service

 order_info
 order_item


payment-service

 payment_record
 payment_channel
```

而不是：

```
main_db

 user_account
 order_info
 payment_record
```

原因：微服务核心思想是**数据自治**，所以：

```
服务边界 = 数据库边界
```

这时候表名前缀需求降低。

## 后缀规则

### 关于后缀，比如 `_tbl`、`_table`

不推荐。例如：

```
user_table
order_table
task_tbl
```

这种很少见。原因：表已经知道是表。类似：

```
user_table
```

相当于：

```
汽车_vehicle
```

信息重复。

## 单复数规则

建议统一。

### 方案A：单数（推荐国内企业，OWL 采用此方案）

```
user_account
order_info
product
```

理由：表代表一个实体。类似 Java：

```
User
Order
Product
```

比较自然。

### 方案B：复数（国外很多）

```
users
orders
products
```

例如 Rails 很喜欢。

两者都可以。大型项目关键是：**全局统一**。

## 表名推荐规范

OWL 采用：

```
{领域}_{业务实体}
```

### 用户域

```
user_account
user_role
user_login_log
```

### 数据标注平台

```
dataset_info
dataset_file
label_task
label_annotation
label_quality_check
```

### AI Agent 平台

```
agent_info
agent_skill
agent_memory
agent_execution_log
```

## 日志表特殊处理

日志类：

```
xxx_log
```

例如：

```
user_login_log
agent_execution_log
api_request_log
```

历史记录：

```
xxx_history
```

例如：

```
workflow_history
```

流水：

```
xxx_record
```

例如：

```
payment_record
```

## 大型项目推荐最终规则

| 场景               | 命名                           |
| ------------------ | ------------------------------ |
| 一个系统一个数据库 | 模块前缀                       |
| 多个项目共享数据库 | 项目+模块前缀                  |
| 微服务             | 服务独立数据库，不加项目名前缀 |
| 表类型             | 不用 tbl/table 后缀            |
| 日志               | xxx_log                        |
| 历史               | xxx_history                    |
| 关系表             | xxx_xxx                        |

例如：

```
label_task
label_dataset
label_annotation

user_account
user_permission

agent_instance
agent_memory
agent_execution_log
```

## OWL 当前模块命名

对于 OWL 的数据标注平台 + Agent 平台这种类型，建议采用：

```
user_xxx
dataset_xxx
label_xxx
task_xxx
agent_xxx
model_xxx
```

这种领域化命名，会比较适合后续扩展到大型平台。

---

# 第二部分：字段命名

## 核心原则

1. 使用小写字母 + 下划线分隔（snake_case）。
2. 字段名自解释，不依赖上下文理解含义。
3. 同一概念在整个数据库中使用统一字段名，例如 `user_id` 不在不同表中写成 `uid`、`userid`、`user`。
4. 字段名不使用数据库关键字和保留字（如 `order`、`group`、`key`、`index`）。

## 通用字段规范

### 主键

```
id
```

- 所有表统一使用 `id` 作为自增主键，类型为 `BIGINT`。
- 不使用 `{表名}_id` 作为主键（如 `user_id` 作为 user 表的主键）。
- 不使用 UUID 字符串作为主键，除非业务明确需要分布式 ID。

### 外键

```
{关联表名}_id
```

| 示例          | 说明             |
| ------------- | ---------------- |
| `user_id`     | 关联 user 表     |
| `address_id`  | 关联 address 表  |
| `watch_id`    | 关联 watch 表    |
| `source_id`   | 关联 source 表   |
| `item_id`     | 关联 item 表     |
| `location_id` | 关联 location 表 |

- 外键字段名与关联表名一致，去掉模块前缀。
- 例如关联 `caishen_fund_watch` 表的外键用 `watch_id`，而非 `caishen_fund_watch_id`。

### 业务编码

```
{业务含义}_code
```

| 示例        | 说明     |
| ----------- | -------- |
| `user_code` | 用户编号 |
| `fund_code` | 基金代码 |

- 用于业务层面的唯一标识，与主键 `id` 区分。
- 通常有唯一索引约束。

### 名称

```
{业务含义}_name
```

| 示例            | 说明       |
| --------------- | ---------- |
| `username`      | 用户名     |
| `fund_name`     | 基金名称   |
| `receiver_name` | 收件人姓名 |
| `source_name`   | 数据源名称 |
| `role_name`     | 角色名称   |

- 当表内只有一个名称字段时，可用 `name`；多个名称字段时加前缀区分。

### 时间字段

| 格式       | 用途           | 示例                                           |
| ---------- | -------------- | ---------------------------------------------- |
| `xxx_time` | 日期时间       | `create_time`、`update_time`、`effective_time` |
| `xxx_date` | 纯日期         | `start_date`、`end_date`、`nav_date`           |
| `xxx_at`   | 事件发生的时刻 | `last_triggered_at`、`last_login_at`           |

- `create_time` 和 `update_time` 是每张表的标准字段，必须存在。
- 禁止使用 `c_time`、`u_time`、`ctime`、`utime` 等缩写。

### 状态字段

```
status
```

- 统一使用 `status`，不使用 `state`、`is_active`、`is_deleted`。
- 值的含义在 `@Schema(description)` 中写明，例如 `"状态：0=正常，1=封禁"`。
- 逻辑删除使用 `status` 的一个值，不单独创建 `is_deleted` 字段。

### 布尔字段

```
is_{含义}
```

| 示例         | 说明     |
| ------------ | -------- |
| `is_default` | 是否默认 |

- 使用 `TINYINT(1)`，值 0 或 1。
- 禁止使用 `has_`、`can_` 开头（除非确实表达能力而非状态）。

### 数量/金额字段

| 格式            | 示例                       |
| --------------- | -------------------------- |
| `{含义}_count`  | `view_count`               |
| `{含义}_amount` | `total_amount`             |
| `{含义}_price`  | `unit_price`               |
| `{含义}`        | `price`、`threshold_value` |

- 金额字段使用 `DECIMAL`，禁止使用 `FLOAT` 或 `DOUBLE`。
- 百分比字段使用 `{含义}_percent` 或 `{含义}_rate`，例如 `daily_return_rate`、`threshold_percent`。

### URL 字段

```
{含义}_url
```

| 示例         | 说明       |
| ------------ | ---------- |
| `avatar_url` | 头像 URL   |
| `image_url`  | 图片 URL   |
| `source_url` | 数据源 URL |

### 备注/描述字段

```
remark              -- 短备注
description         -- 长描述/内容
```

- `remark` 用于简短备注（管理员备注、用户备注），通常 `VARCHAR(500)`。
- `description` 用于较长内容（如商品描述、焦点特性描述），通常 `VARCHAR(2000)` 或 `TEXT`。
- 不在表中创建 `memo`、`note`、`comment` 等字段替代 `remark`。

### 排序字段

```
sort_order
```

- 统一使用 `sort_order`，不使用 `sort`、`order_num`、`seq`。

### JSON 字段

```
{含义}_snapshot    -- 快照数据
{含义}_json        -- 通用 JSON
```

| 示例              | 说明          |
| ----------------- | ------------- |
| `metric_snapshot` | 指标快照 JSON |

- JSON 字段名明确表达内容性质，不直接叫 `data`、`extra`、`json`。

## 字段命名对照表

| 概念     | 统一字段名    | 禁止使用                          |
| -------- | ------------- | --------------------------------- |
| 主键     | `id`          | `uid`、`pk_id`、`auto_id`         |
| 创建时间 | `create_time` | `c_time`、`ctime`、`created_at`   |
| 更新时间 | `update_time` | `u_time`、`utime`、`updated_at`   |
| 状态     | `status`      | `state`、`is_active`、`is_delete` |
| 排序     | `sort_order`  | `sort`、`order_num`、`seq`        |
| 备注     | `remark`      | `memo`、`note`、`comment`         |
| 描述     | `description` | `desc`（MySQL 关键字）、`intro`   |
| 用户 ID  | `user_id`     | `uid`、`userid`                   |
| 用户名   | `username`    | `user_name`、`name`               |
| 密码     | `password`    | `pwd`、`passwd`、`pass`           |
| 手机号   | `phone`       | `mobile`、`tel`、`phone_num`      |
| 邮箱     | `email`       | `mail`、`e_mail`                  |
| 头像     | `avatar_url`  | `avatar`、`head_img`、`portrait`  |
| 昵称     | `nickname`    | `nick`、`nick_name`               |

## 禁止事项

1. 禁止字段名使用大写字母。
2. 禁止字段名使用拼音或中英混合。
3. 禁止字段名使用数据库关键字（如 `order`、`group`、`key`、`index`、`desc`）。
4. 禁止使用 `type`、`name`、`data` 等过于通用的单词作为字段名，除非加上业务前缀。
5. 禁止外键字段名省略 `_id` 后缀。
6. 禁止布尔字段名以 `is_` 之外的前缀开头。
7. 禁止时间字段使用 `int` 或 `varchar` 存储时间戳。

---

# 第三部分：总结

大型项目里：

> **优先使用"领域/模块前缀"，不要使用无意义后缀；不要加项目名前缀，除非多个项目共享数据库。**
>
> **字段命名统一 snake_case，自解释、不缩写、不重复。**

比较成熟的风格：

```
数据库:
owl

表:
agent_info
agent_skill
dataset_info
label_task
user_account

字段:
id
user_id
fund_code
create_time
update_time
is_default
```

## 变更历史

- 2026-08-25：创建数据库表命名规范。
- 2026-08-27：增加字段命名规范，整体移入 `naming/` 子目录。

## 相关文档

- [OWL 文档中心](../../README.md)
- [OWL 文档编写规范](../documentation-standard.md)
- [代码文件与包名命名规范](code-file-naming-convention.md)
- [代码标识符命名规范](code-identifier-naming-convention.md)
- [元数据注解命名规范](metadata-annotation-naming-convention.md)
- [OWL 数据库目录](../../database/OWL/README.md)
