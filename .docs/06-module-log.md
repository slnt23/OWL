# log 模块：业务日志

## 定位

负责业务日志的记录与消费处理，通过 AOP 切面 + 注解方式实现无侵入的业务日志记录。监听消息队列处理日志持久化。

## 包结构

```
xyz.nanian.owl.log
├── config/
│   └── LogAutoConfig.java         ← 日志模块自动配置
├── logging/
│   ├── BizLog.java                ← @BizLog 注解（标记需要记录日志的方法）
│   ├── BizLogAspect.java          ← AOP 切面（拦截 @BizLog 并发送日志消息）
│   └── TraceIdFilter.java         ← TraceId 过滤器（全链路追踪）
├── domain/
│   ├── dto/BizLogMessageDTO.java   ← 日志消息 DTO
│   ├── entity/BizLogDO.java       ← 日志数据库实体
│   └── vo/                        ← 值对象（待扩展）
├── mapper/
│   └── BizLogMapper.java          ← 日志持久化 Mapper
└── service/
    ├── handleBizLogService.java         ← 日志处理接口
    └── impl/handleBizLogServiceImpl.java ← 日志处理实现
```

## 依赖

- `common`
- spring-boot-starter-aop
- jakarta.servlet-api（TraceId 过滤）

## 设计要点

- `@BizLog` 注解标记需要记录的业务方法
- AOP 切面拦截注解方法，收集参数、返回值、异常信息
- 通过消息队列异步发送日志消息，避免阻塞主流程
- `TraceIdFilter` 在请求入口生成 traceId，实现全链路日志追踪
