# OWL (猫头鹰)

以开创多功能、发散性思维的 Java 项目，基于 **Spring Boot 3.3.13** + **Java 17**。

## 技术栈

| 类别 | 技术                            |
|------|-------------------------------|
| 框架 | Spring Boot 3.3.13, Spring Cloud Alibaba (Nacos) |
| 构建 | Maven 3.9.16                  |
| ORM | MyBatis-Plus 3.5.5            |
| 数据库 | MySQL 8.4                     |
| 缓存 | Redis 6.2.7 (Sentinel 集群)     |
| 消息队列 | RabbitMQ 3.12, RocketMQ (规划中) |
| 对象存储 | MinIO 8.5.10                  |
| AI | Spring AI 1.1.4 (OpenAI 兼容)   |
| 接口文档 | Knife4j 4.5.0                 |
| 容器 | Docker 26.1.3                 |

## 项目模块

```
OWL
 ├── common              ← 基础设施层（工具、缓存、MQ、JWT、MinIO）
 ├── domain              ← 领域层（共享 DTO）
 ├── log                 ← 日志层（AOP 业务日志 + TraceId）
 ├── api                 ← 对外接口契约层
 ├── watermelon/         ← 业务父模块
 │   ├── user            ← 用户中心
 │   ├── administration  ← 后台管理
 │   ├── sugarcane       ← 价多多（价格追踪）
 │   ├── crow            ← 乌鸦（AI 对话）
 │   └── pitaya          ← 火龙果（电商平台）
 └── start               ← 启动模块
```

## 模块依赖链

```
common → domain → log → api → watermelon/*
                              ↑
common ──────────────────── start (聚合启动)
```

## 快速开始

```bash
# 编译
./mvnw clean package -DskipTests

# 启动（需先启动 MySQL/Redis/RabbitMQ/Nacos/MinIO）
java -jar start/target/start-0.0.1-SNAPSHOT.jar
```

## 详细文档

完整架构说明、模块详情、部署指南见 [.docs/](./.docs/) 目录：

| 文档 | 内容 |
|------|------|
| [01-项目总览](./.docs/01-project-overview.md) | 技术栈、模块架构、依赖关系 |
| [02-common 模块](./.docs/02-module-common.md) | 基础设施、工具类、拦截器 |
| [03-domain 模块](./.docs/03-module-domain.md) | 领域模型中心 |
| [04-api 模块](./.docs/04-module-api.md) | 对外接口层 |
| [05-start 模块](./.docs/05-module-start.md) | 启动入口、配置文件 |
| [06-log 模块](./.docs/06-module-log.md) | AOP 业务日志 |
| [07-watermelon 总览](./.docs/07-watermelon-overview.md) | 业务模块架构 |
| [08-user 模块](./.docs/08-watermelon-user.md) | 用户中心 |
| [09-administration 模块](./.docs/09-watermelon-administration.md) | 后台管理 |
| [10-sugarcane 模块](./.docs/10-watermelon-sugarcane.md) | 价格追踪 |
| [11-crow 模块](./.docs/11-watermelon-crow.md) | AI 对话 |
| [12-pitaya 模块](./.docs/12-watermelon-pitaya.md) | 电商平台 |
| [13-部署指南](./.docs/13-deployment.md) | 环境要求、部署步骤 |

## 设计目标

- 会当凌绝顶
- 下一趟：爬虫 + Vue3 + 价多多

## 联系方式

Email: relax271828@petalmail.com
