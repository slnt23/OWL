# OWL (猫头鹰) - 项目总览

## 项目简介

OWL 是一个基于 Spring Boot 3 的 Java 多模块项目，以开创多功能、发散性思维为宗旨，融合了电商、AI 对话、价格追踪等多种业务场景。

## 技术栈

| 类别 | 技术 | 版本 |
|------|------|------|
| 语言 | Java | 17 |
| 框架 | Spring Boot | 3.3.13 |
| 微服务 | Spring Cloud Alibaba (Nacos) | 2023.0.3.4 |
| ORM | MyBatis-Plus | 3.5.5 |
| 数据库 | MySQL | 8.4 |
| 缓存 | Redis (Sentinel 集群) | 6.2.7 |
| 消息队列 | RabbitMQ | 3.12-management |
| 消息队列(规划) | RocketMQ | 2.2.3 |
| 对象存储 | MinIO | 8.5.10 |
| AI | Spring AI (OpenAI 兼容) | 1.1.4 |
| 接口文档 | Knife4j (Swagger) | 4.5.0 |
| 对象映射 | MapStruct | 1.6.3 |
| 连接池 | Druid | 1.2.25 |
| 认证 | JWT | 0.11.5 |
| Excel | EasyExcel | 4.0.3 |
| 构建 | Maven (多模块) | - |
| 容器 | Docker | 26.1.3 |

## 模块架构

```
OWL (父 POM)
 ├── common              ← 基础设施层：工具类、缓存、MQ、JWT、MinIO
 ├── domain              ← 领域层：共享 DTO
 ├── log                 ← 日志层：AOP 业务日志 + TraceId
 ├── api                 ← 接口层：对外 Feign 接口契约（规划中）
 ├── watermelon          ← 业务父模块 (POM)
 │   ├── user            ← 用户模块：登录注册、角色权限
 │   ├── administration  ← 管理模块：后台管理
 │   ├── sugarcane       ← 价多多：价格追踪管理
 │   ├── crow            ← 乌鸦：AI 对话模块
 │   └── pitaya          ← 火龙果：电商模块
 └── start               ← 启动模块：Spring Boot 入口
```

## 模块依赖关系

```
common ← domain ← log ← api ← watermelon/*
                           ↑
common ←───────────────── start (启动入口，聚合所有模块)
```

- `common` 不依赖任何业务模块，被所有模块依赖
- `domain` 依赖 `common`，存放核心业务模型
- `log` 依赖 `common`，AOP 日志独立模块
- `api` 依赖 `domain` + `log`，定义对外接口
- `watermelon/*` 各业务模块依赖 `api`
- `start` 聚合所有模块，提供启动入口

## 配置管理

项目采用 **Nacos** 作为配置中心：

- Nacos Server: `192.168.131.128:8848`
- Namespace: `OWL`
- 环境: `dev` / `prod`
- 本地配置文件仅保留最小化配置，运行时配置全部从 Nacos 拉取

## 官方邮箱

Email: relax271828@petalmail.com
