# OWL 代码文件与包名命名规范

| 属性     | 值         |
| -------- | ---------- |
| 状态     | 已批准     |
| 负责人   | 仓库 Owner |
| 创建时间 | 2026-08-27 |
| 更新时间 | 2026-08-27 |
| 适用版本 | 全部       |

## 背景与目标

OWL 是多模块 Maven 项目，每个模块按分层架构组织代码。本文档约束 Java 文件、包路径、资源文件和目录的命名规则，目标是让开发者一眼定位文件所属模块、所属分层和业务含义。

## 包命名规范

### 基础包名

```
xyz.nanian.owl.{模块名}.{分层}
```

| 组成部分 | 规则                                 | 示例                              |
| -------- | ------------------------------------ | --------------------------------- |
| 根包     | `xyz.nanian.owl`，全局固定           | 所有模块一致                      |
| 模块名   | 小写英文，与 Maven `artifactId` 一致 | `user`、`admin`、`caishen`        |
| 分层     | 按架构分层，见下方分层包名表         | `controller`、`service`、`mapper` |

### 分层包名表

| 分层       | 包名            | 存放内容                                |
| ---------- | --------------- | --------------------------------------- |
| 控制器     | `controller`    | `XxxController`                         |
| 服务接口   | `service`       | `XxxService`（接口）                    |
| 服务实现   | `service.impl`  | `XxxServiceImpl`（实现类）              |
| 数据访问   | `mapper`        | `XxxMapper`（MyBatis-Plus Mapper 接口） |
| 实体       | `domain.entity` | `XxxDO`（数据对象实体）                 |
| 入参 DTO   | `domain.dto`    | `XxxDTO`（请求入参）                    |
| 出参 VO    | `domain.vo`     | `XxxVO`（响应出参）                     |
| 查询对象   | `domain.query`  | `XxxQuery`（分页/筛选查询条件）         |
| 对象转换   | `mapstruct`     | `XxxConvert`（MapStruct 转换器接口）    |
| 常量       | `constant`      | `XxxConstant`（常量类）                 |
| 工具类     | `utils`         | `XxxUtil`（工具类）                     |
| 配置类     | `config`        | `XxxConfig`（配置类）                   |
| 调度任务   | `scheduler`     | `XxxScheduler`（定时任务）              |
| 外部客户端 | `client`        | `XxxClient`（远程调用客户端）           |

### 实际示例

```
user 模块：
xyz.nanian.owl.user.controller      → UserController
xyz.nanian.owl.user.service         → UserService（接口）
xyz.nanian.owl.user.service.impl    → UserServiceImpl
xyz.nanian.owl.user.mapper          → UserMapper
xyz.nanian.owl.user.domain.entity   → UserDO
xyz.nanian.owl.user.domain.dto      → EmailLoginDTO
xyz.nanian.owl.user.domain.vo       → UserInfoVO
xyz.nanian.owl.user.mapstruct       → UserConvert
xyz.nanian.owl.user.constant        → UserConstant
xyz.nanian.owl.user.utils           → CodeCacheUtil

caishen 模块：
xyz.nanian.owl.caishen.controller   → FundController
xyz.nanian.owl.caishen.service      → FundService（接口）
xyz.nanian.owl.caishen.service.impl → FundServiceImpl
xyz.nanian.owl.caishen.mapper       → CaishenFundMapper
xyz.nanian.owl.caishen.domain.entity→ CaishenFundDO
xyz.nanian.owl.caishen.domain.dto   → FundWatchCreateDTO
xyz.nanian.owl.caishen.domain.vo    → FundVO
xyz.nanian.owl.caishen.mapstruct    → CaishenFundConvert
xyz.nanian.owl.caishen.constant     → AlertType
xyz.nanian.owl.caishen.scheduler    → CaishenMonitorScheduler
xyz.nanian.owl.caishen.client       → MarketDataClient
```

## Java 文件命名规范

### 通用规则

1. 使用 UpperCamelCase（大驼峰），首字母大写。
2. 文件名必须与唯一的顶层 `public` 类/接口/枚举名一致。
3. 一个文件只放一个顶层类型；内部类不在此限。
4. 禁止使用拼音、缩写堆砌；业务术语允许公认缩写（如 `DTO`、`VO`、`DO`、`JWT`）。

### 按类型分类

| 类型         | 命名格式              | 示例                                      |
| ------------ | --------------------- | ----------------------------------------- |
| Controller   | `{业务名}Controller`  | `UserController`                          |
| Service 接口 | `{业务名}Service`     | `UserService`                             |
| Service 实现 | `{业务名}ServiceImpl` | `UserServiceImpl`                         |
| Mapper       | `{业务名}Mapper`      | `UserMapper`                              |
| DO 实体      | `{业务名}DO`          | `UserDO`、`UserAddressDO`                 |
| DTO 入参     | `{动作}DTO`           | `EmailLoginDTO`、`AddressCreateDTO`       |
| VO 出参      | `{业务名}VO`          | `UserInfoVO`、`AddressVO`                 |
| Query 查询   | `{业务名}Query`       | `OrderDateQuery`、`FundNavPageQueryDTO`   |
| Convert      | `{业务名}Convert`     | `UserConvert`、`AddressConvert`           |
| Constant     | `{业务名}Constant`    | `UserConstant`、`LoginConstant`           |
| Config       | `{功能名}Config`      | `WebMvcConfig`、`MybatisPlusConfig`       |
| Util         | `{功能名}Util`        | `CodeCacheUtil`、`RegexUtil`              |
| Exception    | `{异常名}Exception`   | `BizException`、`LoginFailureException`   |
| Scheduler    | `{业务名}Scheduler`   | `CaishenMonitorScheduler`                 |
| Client       | `{业务名}Client`      | `MarketDataClient`、`AssetAnalysisClient` |

### 跨模块同名类处理

当不同模块需要同名 DO 时（如 `UserDO` 在 `user` 和 `admin` 模块），使用不同包路径区分（`xyz.nanian.owl.user.domain.entity.UserDO` 与 `xyz.nanian.owl.admin.domain.entity.UserDO`），不采用前缀。

但如果同一模块内需要区分不同数据源的实体，可使用模块前缀，例如：

```
caishen 模块：
CaishenFundDO        → 基金表实体
CaishenFundNavDO     → 基金净值表实体
CaishenFundAlertDO   → 基金提醒表实体
```

## 目录结构规范

### 业务模块标准目录树

```
src/main/java/xyz/nanian/owl/{模块名}/
├── client/                    # 外部服务客户端（可选）
│   └── mock/                  # Mock 实现（可选）
├── config/                    # 模块配置类（可选）
├── constant/                  # 常量与枚举（可选）
├── controller/                # REST 控制器
├── domain/
│   ├── dto/                   # 请求入参
│   ├── entity/                # 数据库实体
│   ├── query/                 # 查询条件对象（可选）
│   └── vo/                    # 响应出参
├── mapper/                    # MyBatis-Plus Mapper 接口
├── mapstruct/                 # MapStruct 对象转换器（可选）
├── scheduler/                 # 定时任务（可选）
└── service/
    ├── impl/                  # 服务实现类
    └── XxxService.java        # 服务接口
```

### 资源文件

```
src/main/resources/
├── mapper/                    # MyBatis Mapper XML
│   ├── UserMapper.xml
│   └── UserAddressMapper.xml
├── application.yml            # 或 application-{profile}.yml
└── ...
```

### 目录命名规则

1. 目录名使用小写英文，多个单词使用连字符或直接拼接（以项目现有习惯为准）。
2. 分层目录名固定，不可自定义，例如必须用 `controller` 而非 `controllers`。
3. 业务模块根目录名与 Maven `artifactId` 一致，使用小写英文。

## 资源文件命名规范

### Mapper XML

| 规则                              | 示例                                 |
| --------------------------------- | ------------------------------------ |
| 与 Mapper 接口同名                | `UserMapper.java` → `UserMapper.xml` |
| 存放在 `resources/mapper/` 目录下 | `resources/mapper/UserMapper.xml`    |

### 配置文件

| 规则              | 示例                                          |
| ----------------- | --------------------------------------------- |
| 主配置            | `application.yml`                             |
| 环境配置          | `application-dev.yml`、`application-prod.yml` |
| 使用小写 + 连字符 | 不使用 `Application-Dev.yml`                  |

## 禁止事项

1. 禁止文件名使用下划线（`_`）分隔单词，Java 文件使用驼峰，资源文件使用连字符（`-`）。
2. 禁止在包名中使用大写字母。
3. 禁止将不同分层的类混放在同一包下。
4. 禁止使用 `impl` 作为顶层包名；`impl` 只作为 `service.impl` 子包存在。
5. 禁止在 `domain` 下创建除 `dto`、`entity`、`vo`、`query` 之外的子包；如需新增，先更新本规范。

## 变更历史

- 2026-08-27：创建代码文件与包名命名规范。

## 相关文档

- [OWL 文档中心](../../README.md)
- [OWL 文档编写规范](../documentation-standard.md)
- [代码标识符命名规范](code-identifier-naming-convention.md)
- [元数据注解命名规范](metadata-annotation-naming-convention.md)
- [OWL 数据库命名规范](database-naming-convention.md)
