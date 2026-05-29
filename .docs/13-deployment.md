# 部署指南

## 环境要求

| 组件 | 版本 | 说明 |
|------|------|------|
| JDK | 17+ | 编译和运行 |
| Maven | 3.6+ | 构建工具 |
| Docker | 26.1.3 | 容器化部署 |
| MySQL | 8.4 | 主数据库 |
| Redis | 6.2.7 | 哨兵集群模式 |
| RabbitMQ | 3.12-management | 消息队列 |
| Nacos | 2.x | 配置中心 + 服务发现 |
| MinIO | latest | 对象存储 |

## Redis 哨兵集群

哨兵模式，共 6 个节点：
- Sentinel: `7001`, `7002`, `7003`
- Redis 实例: `27001`, `27002`, `27003`

## RabbitMQ

- 管理插件已启用
- 用户: `admin`
- 密码: `123456qin`

## Nacos 配置

- 地址: `192.168.131.128:8848`
- Namespace: `OWL`
- 所有运行时配置从 Nacos 拉取（DB 连接、Redis、MQ、AI、邮件等）

## 配置文件层级

```
start/src/main/resources/
├── application.yaml        ← 主配置（导入 Nacos，设置 profile）
├── application-dev.yml     ← 开发环境配置
├── application-prod.yml    ← 生产环境配置（使用环境变量）
└── bootstrap.yml           ← Nacos 引导配置
```

## 启动步骤

1. 确保 MySQL、Redis、RabbitMQ、Nacos、MinIO 已启动
2. 在 Nacos 中配置各环境的配置文件
3. 执行数据库初始化脚本
4. 编译打包：
   ```bash
   ./mvnw clean package -DskipTests
   ```
5. 启动应用：
   ```bash
   java -jar start/target/start-0.0.1-SNAPSHOT.jar
   ```

## Docker 部署（规划）

```yaml
# 预计使用 Docker Compose 编排所有服务
# - MySQL 容器
# - Redis Sentinel 集群容器
# - RabbitMQ 容器
# - Nacos 容器
# - MinIO 容器
# - OWL 应用容器
```
