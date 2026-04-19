# 公共类

## common 模块：公共工具 & 通用组件

- 作用：存放 整个项目通用的工具类、常量、异常、配置、工具包。所有模块都可以依赖它。
- 典型内容：

  - `util` 包：DateUtil、StringUtil、JsonUtil 等。
  - `constant` 包：枚举、错误码（如 ErrorCodeEnum）。
  - `exception` 包：全局异常类（如 BusinessException）。
  - `config` 包：Swagger 配置、Redis 配置、线程池配置。

- 依赖关系：不依赖任何业务模块，被 `api/domain` 等依赖。
- 为什么需要？
避免代码重复。比如所有模块都需要 JSON 序列化工具，就放 common 里，一改全用。


## 工具

- slf4j：springboot 项目自动包含，日志的外显接口
- logback: springboot 项目自动包含，打印日志的实体类

- 对于infrastructure层的工具类，建议放在common模块中，供整个项目使用。比如：
  后续可以拆开成微服务，后续可以拆开成微服务，目前集中到这里