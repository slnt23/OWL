# watermelon 业务模块总览

西瓜（watermelon）是集成管理所有业务模块的父模块，本身为 POM 类型，不包含代码。各子模块统一依赖 `api` 模块，遵循相同的分层架构。

## 子模块一览

| 模块 | 中文名 | 定位 | 主要功能 |
|------|--------|------|----------|
| `user` | 用户 | 用户中心 | 登录注册、角色权限、地址管理 |
| `administration` | 管理 | 后台管理 | 用户管理、角色管理、日志管理、焦点/特性管理 |
| `sugarcane` | 价多多 | 价格追踪 | 商品价格采集、多源比价、趋势分析 |
| `crow` | 乌鸦 | AI 对话 | AI 聊天、会话管理、技能执行 |
| `pitaya` | 火龙果 | 电商平台 | 消费者端（购物车/订单）、商家端（商品/订单管理） |

## 统一分层架构

每个业务子模块遵循相同的分层模式：

```
controller/     ← RESTful 接口，参数校验，响应组装
  ├── domain/
  │   ├── dto/       ← 请求/响应数据传输对象
  │   ├── entity/    ← 数据库实体（MyBatis-Plus）
  │   ├── vo/        ← 视图对象（返回前端）
  │   └── query/     ← 查询条件对象
  ├── mapper/           ← 数据访问层（MyBatis-Plus BaseMapper）
  ├── mapstruct/        ← 对象转换器（MapStruct Convert）
  ├── service/          ← 业务接口
  └── service/impl/     ← 业务实现
```

## 公共约定

- 所有模块统一使用 `xyz.nanian.owl.<module>` 作为基础包名
- 每个模块包含 `CodeGenerator.java` 用于 MyBatis-Plus 代码生成
- MyBatis XML 映射文件统一放在 `src/main/resources/mapper/` 下
- 管理类接口以 `Admin` 结尾，需要管理员权限
