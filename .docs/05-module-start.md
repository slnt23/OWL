# start 模块：启动模块

## 定位

多模块项目的唯一启动入口。Spring Boot 主类所在，聚合加载所有业务模块。

## 结构

```
xyz.nanian.owl
└── StartApplication.java    ← @SpringBootApplication 主启动类
                                @EnableRabbit 启用 RabbitMQ
```

## 配置文件

```
src/main/resources/
├── application.yaml         ← 主配置（导入 Nacos 配置）
├── application-dev.yml      ← 开发环境（本地 MySQL/Redis/RabbitMQ/AI）
├── application-prod.yml     ← 生产环境（环境变量注入）
├── bootstrap.yml            ← Nacos 引导配置（已注释）
└── logback-spring.xml       ← 日志配置（含 traceId 输出）
```

## 依赖

聚合所有模块：
- `common`
- `user`
- `pitaya`
- `sugarcane`
- `crow`
- `administration`
- spring-boot-starter-test
- spring-boot-starter-mail

## 测试

| 测试类 | 用途 |
|--------|------|
| `TestEmail.java` | 邮件发送测试 |
| `ProductTest.java` | 商品功能测试 |
| `RabbitMQTest.java` | 消息队列测试 |
