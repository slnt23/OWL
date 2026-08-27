# OWL 代码标识符命名规范

| 属性     | 值         |
| -------- | ---------- |
| 状态     | 已批准     |
| 负责人   | 仓库 Owner |
| 创建时间 | 2026-08-27 |
| 更新时间 | 2026-08-27 |
| 适用版本 | 全部       |

## 背景与目标

本文档约束 OWL 仓库中所有 Java 代码标识符的命名规则，包括类、接口、枚举、方法、变量和常量。目标是让命名自解释、可搜索、可预测，减少沟通成本和理解偏差。

## 通用原则

1. 命名必须表达业务含义，禁止使用 `a`、`b`、`temp`、`data`、`obj` 等无意义名称。
2. 使用英文全称，禁止拼音；公认的行业缩写可保留（如 `DTO`、`VO`、`DO`、`API`、`URL`）。
3. 命名长度与作用域成正比：字段名可长，局部变量可短，循环变量可用 `i`、`j`。
4. 同一概念在整个项目中必须使用统一的英文单词，例如「用户」统一用 `user`，不同时出现 `member`、`account`。
5. 禁止使用前缀/后缀来表达类型（即匈牙利命名法），例如 `strName`、`iCount`、`bFlag`。

## 类命名

### Controller 类

```
格式：{业务名}Controller
示例：UserController、UserAddressController、LoginController
     FundController、WatchController、AlertController
```

- 业务名使用名词，表示该类管理的资源。
- 一个 Controller 管理一个聚合根资源；子资源可独立 Controller 或合并在父 Controller。
- 面向 C 端与后台管理的 Controller 分属不同模块，通过包名区分。

### Service 接口

```
格式：{业务名}Service
示例：UserService、UserAddressService、LoginService
     FundService、AlertService、WatchService
```

- 业务名与 Controller 对应。
- 接口定义在 `service` 包，实现类在 `service.impl` 包。

### Service 实现类

```
格式：{业务名}ServiceImpl
示例：UserServiceImpl、UserAddressServiceImpl、LoginServiceImpl
     FundServiceImpl、AlertServiceImpl
```

- 实现类名 = 接口名 + `Impl`。
- 必须实现对应的 Service 接口，不可跳过接口直接写实现类。
- 特殊情况：如果实现的是来自 common 或 infra 模块的接口，名称可体现具体技术，例如 `RedisTokenRevocationServiceImpl`。

### Mapper 接口

```
格式：{业务名}Mapper
示例：UserMapper、UserAddressMapper
     CaishenFundMapper、CaishenFundNavMapper
```

- 业务名对应实体名，一般与 DO 名一致。
- 跨模块时必须加模块前缀，例如 `CaishenFundMapper`（区别于 `user` 模块的 `FundMapper`）。

### DO 实体类

```
格式：{表名转驼峰}DO
示例：UserDO、UserAddressDO
     CaishenFundDO、CaishenFundNavDO、CaishenFundAlertDO
```

- 表名转驼峰：`user_account` → `UserAccount`，加上 `DO` 后缀 → `UserAccountDO`。
- 与数据库表名一一对应。
- 同模块内多张同类表时加模块前缀，例如 `caishen` 模块的 `CaishenFundDO`。

### DTO 入参类

```
格式：{动作}{业务名}DTO
示例：EmailLoginDTO、PasswordLoginDTO、AddressCreateDTO、AddressUpdateDTO
     FundWatchCreateDTO、FundWatchUpdateDTO、FundAlertCreateDTO
```

- 动作在前：`SendCode`、`ResetPassword`、`Create`、`Update`、`Delete`、`Query`、`Bind`。
- 动作与业务名组合清晰表达「这个 DTO 用于什么操作」。
- 禁止使用 `Param`、`Request`、`Req` 等后缀替代 `DTO`。

### VO 出参类

```
格式：{业务名}VO
示例：UserInfoVO、AddressVO
     FundVO、FundNavVO、FundAlertVO、FundWatchVO、SummaryVO
```

- 业务名描述返回的数据内容，不描述操作。
- 可以包含嵌套 VO，嵌套 VO 放在同一文件或同包内。

### Query 查询类

```
格式：{业务名}Query
示例：OrderDateQuery、SummaryQueryDTO、FundNavPageQueryDTO
```

- 用于封装分页和筛选条件；与 DTO 不同，Query 不直接对应一个 API 入参。
- 如果查询条件简单（如只有分页参数），可复用 common 模块的 `PageDTO`。

### Convert 转换器接口

```
格式：{业务名}Convert
示例：UserConvert、AddressConvert
     CaishenFundConvert、CaishenFundAlertConvert、CaishenSummaryConvert
```

- MapStruct 转换器接口，业务名通常对应 DO 实体名。
- 跨模块时加模块前缀。

### Constant 常量类

```
格式：{业务名}Constant
示例：UserConstant、LoginConstant
```

- 存放与特定业务相关的字符串常量、数字常量。
- 枚举类型使用独立文件，命名见下方枚举规范。

### Config 配置类

```
格式：{功能名}Config
示例：WebMvcConfig、MybatisPlusConfig、JacksonConfig
     CaishenConfig、CaishenSchedulingConfig
```

- 功能名描述配置内容，例如 `CaishenSchedulingConfig` 表示财神模块的调度配置。
- 模块级配置加模块前缀，全局配置不加。

### Util 工具类

```
格式：{功能名}Util
示例：CodeCacheUtil、RegexUtil
```

- 功能名描述工具用途，不加 `Helper`、`Tool` 等后缀。
- 工具类方法全部 `static`；如果需要有状态，应改为 Service。

### Exception 异常类

```
格式：{异常名}Exception
示例：BizException、LoginFailureException
```

- 继承 `RuntimeException`，异常名描述异常原因。
- 业务异常统一使用 `BizException` 传递状态码，自定义异常类仅在需要特殊处理时创建。

### Scheduler 调度类

```
格式：{业务名}Scheduler
示例：CaishenMonitorScheduler、CaishenSummaryScheduler
```

- 业务名描述定时任务内容。
- 使用 `@Scheduled` 注解的方法放在 Scheduler 类中，不放在 Service 中。

### Client 客户端类

```
格式：{业务名}Client
示例：MarketDataClient、AssetAnalysisClient
```

- 用于封装对第三方 API 或远程服务的调用。
- Mock 实现放在 `client.mock` 包下，命名为 `Mock{业务名}Client`。

## 接口命名

### 通用接口

```
格式：{业务名}Service
示例：UserService、LoginService、MailService
```

### 基础设施接口

```
格式：{能力名}
示例：TokenRevocationService
```

- 定义在 common 模块的接口使用能力名，不加 `I` 前缀。
- 禁止使用 `IXxxService` 这种 C# 风格命名。

### 回调/监听器接口

```
格式：{事件名}Listener 或 {事件名}Handler
示例：无需额外约束，按 Java 生态惯例
```

## 枚举命名

```
格式：{业务名}{Enum后缀可省略}
示例：ResultStatus、AlertStatus、AlertType、PeriodType、SummaryStatus
```

- 枚举类名使用名词，描述枚举值的集合概念。
- 枚举值使用全大写 + 下划线：`ACTIVE`、`TRIGGERED`、`ACKED`。
- 枚举值必须有业务含义，不使用 `UNKNOWN`、`OTHER` 作为兜底值（除非业务确实需要）。

## 方法命名

### Controller 方法

| 操作     | 命名格式                   | 示例                                       |
| -------- | -------------------------- | ------------------------------------------ |
| 查询列表 | `list` / `get{资源名}List` | `getUserList`、`list`                      |
| 查询详情 | `get{资源名}` / `detail`   | `getUserInfo`、`detail`                    |
| 新增     | `create` / `add{资源名}`   | `createAddress`、`add`                     |
| 更新     | `update{资源名}`           | `updateAddress`、`updateInfo`              |
| 删除     | `delete{资源名}`           | `deleteAddress`、`delete`                  |
| 特殊操作 | `{动词}{宾语}`             | `resetPassword`、`bindEmail`、`setDefault` |

- Controller 方法名优先使用动词开头，表达操作意图。
- 通过 `@Operation(summary = "...")` 补充中文描述，方法名本身是英文摘要。

### Service 方法

- 与 Controller 方法名保持一致，或更具体。
- 查询方法：`getXxx`、`listXxx`、`findXxx`、`queryXxx`。
- 写操作方法：`createXxx`、`updateXxx`、`deleteXxx`、`saveXxx`。
- 布尔判断方法：`isXxx`、`hasXxx`、`canXxx`。

### Mapper 方法

- MyBatis-Plus 自带方法（`selectById`、`selectList` 等）不重复定义。
- 自定义方法优先使用 MyBatis-Plus 风格：`selectXxx`、`insertXxx`、`updateXxx`、`deleteXxx`。
- 禁止使用 `getXxx` 命名 Mapper 方法（`get` 是 Service 层风格）。

## 变量命名

### 字段命名

- 使用 lowerCamelCase（小驼峰）。
- 布尔字段使用 `isXxx`、`hasXxx` 开头（Lombok 生成的 getter 为 `isXxx()`）。
- 时间字段使用 `XxxTime`（如 `createTime`、`updateTime`），不使用 `XxxDate` 除非确实只存日期。
- ID 字段使用 `id` 或 `{业务名}Id`（如 `userId`、`addressId`）。

### 局部变量

- 方法内局部变量使用小驼峰，尽量简短但有意义。
- Stream/Lambda 参数使用有意义的单字母或短词，例如 `user -> user.getName()` 而非 `u -> u.getName()`（除非上下文极短）。
- 循环变量可用 `i`、`j`、`k`。

### 参数命名

- 方法参数使用小驼峰，与字段命名规则一致。
- 不使用 `p1`、`p2`、`arg` 等无意义名称。

## 常量命名

```
格式：全大写 + 下划线
示例：MAX_RETRY_COUNT、DEFAULT_PAGE_SIZE
     LOGIN_CODE_PREFIX、TOKEN_BLACKLIST_PREFIX
```

- 使用 `static final` 修饰。
- 常量值不可变；如果值是集合，使用 `Collections.unmodifiableXxx` 包装。
- 常量类中按业务分组，使用注释分隔。

## 泛型命名

- 单字母大写：`T`（类型）、`E`（元素）、`K`（键）、`V`（值）。
- 多个泛型时延续：`T`、`U`、`R`。
- 有明确含义时使用全称，例如 `Result<T>`、`ResultPage<T>`。

## 禁止事项

1. 禁止使用拼音命名任何标识符。
2. 禁止在类名、接口名、枚举名中使用下划线（`_`），除非是常量名。
3. 禁止使用 `$` 符号命名（由编译器生成的除外）。
4. 禁止使用 Java 关键字和保留字作为标识符。
5. 禁止缩写超过 3 个字母的单词，除非是业界公认缩写（如 `DTO`、`VO`、`API`）。
6. 禁止方法名超过 5 个单词；如果超过，说明职责太多，需要拆分。
7. 禁止 `Controller` 方法中出现 `Request`、`Response` 参数名；直接用 `@RequestBody` 绑定到 DTO/VO。

## 变更历史

- 2026-08-27：创建代码标识符命名规范。

## 相关文档

- [OWL 文档中心](../../README.md)
- [OWL 文档编写规范](../documentation-standard.md)
- [代码文件与包名命名规范](code-file-naming-convention.md)
- [元数据注解命名规范](metadata-annotation-naming-convention.md)
- [OWL 代码注释规范](../code-comment-standard.md)
