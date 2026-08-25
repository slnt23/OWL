# OWL 数据库表命名规范

| 属性     | 值         |
| -------- | ---------- |
| 状态     | 已批准     |
| 负责人   | 仓库 Owner |
| 创建时间 | 2026-08-25 |
| 更新时间 | 2026-08-25 |
| 适用版本 | 全部       |

## 背景与目标

对于**大型项目、多模块系统、微服务架构**来说，数据库表命名是否加前缀/后缀，没有绝对答案，但业界有比较成熟的实践：

> **不要为了"看起来区分"而给所有表乱加前缀；应该根据数据库边界、模块边界、生命周期来决定。**

本文档约束 OWL 仓库中所有数据库表的命名规则，目标是让表名一眼可知归属模块、一眼可知业务含义。

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

## 总结

大型项目里：

> **优先使用"领域/模块前缀"，不要使用无意义后缀；不要加项目名前缀，除非多个项目共享数据库。**

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
```

而不是：

```
owl_agent_info
tbl_agent_info
agent_info_table
```

## 变更历史

- 2026-08-25：创建数据库表命名规范。

## 相关文档

- [OWL 文档中心](../README.md)
- [OWL 文档编写规范](documentation-standard.md)
- [OWL 数据库目录](../database/OWL/README.md)
