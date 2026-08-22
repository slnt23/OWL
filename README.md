# OWL（猫头鹰）

![一念神魔](.docs/assets/一念神魔.png)

OWL 是一个多业务 Java 后端项目，基于 Spring Boot 4.1 与 Java 25 构建，覆盖用户认证、AI 对话、价格追踪与后台管理。通用能力沉淀在 `common`、`infra`、`log` 等基础模块，业务模块独立演进。

## 技术栈

| 类别           | 技术                                               |
| -------------- | -------------------------------------------------- |
| 基础           | Java 25、Spring Boot 4.1、Maven 多模块             |
| 数据库与中间件 | MyBatis-Plus、MySQL、Redis、RabbitMQ、MinIO、Nacos |
| AI             | Spring AI（DeepSeek）                              |
| 接口文档       | Knife4j                                            |

## 快速开始

```bash
mvn clean package -DskipTests
java -jar start/target/start-0.0.1.jar
```

启动前需要准备 MySQL、Redis、RabbitMQ、Nacos、MinIO；连接配置默认通过 Nacos 导入。

## 文档

- [文档中心](.docs/README.md)
- [前端联调接口文档](.docs/others/前端联调接口文档.md)
