# common 模块：公共工具 & 通用组件

## 定位

基础设施层，存放整个项目通用的工具类、常量、异常、配置。所有模块都可以依赖它，不依赖任何业务模块。

## 包结构

```
xyz.nanian.owl
├── config/
│   ├── MybatisPlusConfig.java      ← MyBatis-Plus 分页插件配置
│   ├── SpringdocConfig.java        ← Knife4j/Swagger 文档配置
│   └── WebMvcConfig.java           ← Web MVC 配置（CORS 等）
├── constant/
│   ├── JwtConstant.java            ← JWT 相关常量
│   ├── LoginConstant.java          ← 登录相关常量
│   └── MailConstant.java           ← 邮件相关常量
├── exception/
│   ├── BizException.java           ← 通用业务异常
│   ├── GlobalExceptionHandler.java ← 全局异常处理器
│   └── LoginException.java         ← 登录异常
├── interceptor/
│   ├── AuthInterceptor.java        ← 认证拦截器
│   └── LoginInterceptor.java       ← 登录拦截器
├── result/
│   ├── Result.java                 ← 统一响应体
│   ├── ResultPage.java             ← 分页响应体
│   └── ResultStatus.java           ← 响应状态枚举
├── utils/
│   ├── jwt/
│   │   ├── JwtUtil.java            ← JWT 生成/解析工具
│   │   ├── UserContext.java        ← 用户上下文（ThreadLocal）
│   │   └── UserInfo.java           ← 用户信息模型
│   ├── mail/
│   │   └── MailUtil.java           ← 邮件发送工具
│   └── regex/
│       ├── RegexPatterns.java      ← 正则表达式常量
│       └── RegexUtil.java          ← 正则校验工具
└── infrastructure/
    ├── minio/
    │   ├── config/MinioConfig.java
    │   ├── constant/MinioConstant.java
    │   ├── properties/MinioProperties.java
    │   ├── service/FileStorageService.java          ← 文件存储接口
    │   └── service/impl/MinioFileServiceImpl.java   ← MinIO 实现
    ├── rabbitmq/
    │   ├── config/BizMQConfig.java    ← 业务消息队列配置
    │   ├── config/OrderMQConfig.java  ← 订单消息队列配置
    │   └── constant/RabbitMQConstant.java
    ├── redis/
    │   ├── config/RedisConfig.java    ← Redis 序列化/连接配置
    │   └── util/CodeCacheUtil.java    ← 验证码缓存工具
    ├── docker/       ← 规划中
    └── rocketmq/     ← 规划中
```

## 依赖

- Spring AMQP (RabbitMQ)
- Spring Data Redis + Commons Pool2
- Nacos Config & Discovery
- MinIO Client
- Spring AI OpenAI Starter
- JWT (jjwt-api, jjwt-impl, jjwt-jackson)
- JavaMail
- Knife4j (Swagger 文档)
- Druid 连接池
- MyBatis-Plus
- MapStruct
- EasyExcel
- Velocity (模板引擎)

## 设计原则

- 不依赖任何业务模块
- 工具类尽量无状态
- 基础设施层接口与实现分离（如 FileStorageService → MinioFileServiceImpl）
- 后续可拆分为独立微服务
