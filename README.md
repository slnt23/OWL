# OWL（猫头鹰）

OWL 是一个以多业务、多方向方式探索后端能力的 Java 项目，覆盖用户认证、AI 对话、价格追踪、电商交易和后台管理。项目基于 **Spring Boot 3.3.13**、**Java 17** 与 **MyBatis-Plus** 构建，采用多模块 Maven 工程组织，通用能力沉淀在基础模块，业务模块独立演进。

## 项目特色

- **用户中心**：邮箱验证码注册/登录、密码登录、JWT 鉴权、角色与用户地址管理。
- **AI 对话**：基于 Spring AI 接入 DeepSeek，支持流式输出、会话持久化、Token 统计与可配置的 Skill 模板。
- **价格追踪（价多多）**：商品分类、物品、价格来源、地理位置、价格记录、趋势分析与多来源对比。
- **电商平台（火龙果）**：商品、购物车、订单、地址，覆盖消费者与商家两端。
- **后台管理**：用户、角色、业务日志、功能位与聚光灯管理。
- **工程化基础**：统一 `Result<T>` 响应、全局异常处理、JWT + Spring Security 鉴权、AOP 业务日志与 TraceId、Knife4j 接口文档。
- **中间件集成**：MySQL、Redis、RabbitMQ、Nacos、MinIO，以及 Guava 布隆过滤器。
- **可运行与可部署**：Docker 多阶段构建，支持 Nacos 动态配置。

## 技术栈

| 类别 | 技术 |
| --- | --- |
| 框架 | Spring Boot 3.3.13、Spring Cloud Alibaba（Nacos） |
| ORM | MyBatis-Plus 3.5.5 |
| 数据库 | MySQL 8.4 |
| 缓存 | Redis（Sentinel 集群） |
| 消息队列 | RabbitMQ |
| 对象存储 | MinIO |
| AI | Spring AI 1.1.4（OpenAI 兼容协议接入 DeepSeek） |
| 接口文档 | Knife4j 4.5.0 |
| 构建 / 容器 | Maven 3.9.16、Docker |

## 快速开始

```bash
mvn clean package -DskipTests
java -jar start/target/start-0.0.1-SNAPSHOT.jar
```

启动前需准备 MySQL、Redis、RabbitMQ、Nacos、MinIO；连接配置默认通过 Nacos 导入（`optional:nacos:*`），本地开发可在 `start` 模块配置文件中取消注释。
