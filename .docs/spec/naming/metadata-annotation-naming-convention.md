# OWL 元数据注解命名规范

| 属性     | 值         |
| -------- | ---------- |
| 状态     | 已批准     |
| 负责人   | 仓库 Owner |
| 创建时间 | 2026-08-27 |
| 更新时间 | 2026-08-27 |
| 适用版本 | 全部       |

## 背景与目标

OWL 使用 Knife4j（SpringDoc OpenAPI）自动生成 API 文档。Controller 上的 `@Tag`、`@Operation` 和 DTO/VO 上的 `@Schema` 注解是文档的元数据来源，直接影响 API 文档的可读性。

本文档约束这些元数据注解的命名和描述规则，目标是让自动生成的 API 文档清晰、一致、可维护。

## 核心原则

1. 元数据面向使用者，不是面向开发者：用中文写业务含义，不用英文或用技术术语翻译。
2. 同一概念在不同注解中使用统一措辞，例如「用户」不同时写「用户」和「会员」。
3. 先写核心信息，再补充细节描述；summary/name 必须简洁，description 可以展开。
4. 注解内容与代码行为必须一致；行为变化时同步更新注解。

## @Tag 命名规范

### 位置

Controller 类上，用于在 Knife4j 文档中对该 Controller 下所有接口分组。

### 格式

```java
@Tag(name = "{分组名称}", description = "{分组描述}")
```

### 规则

| 属性          | 规则                                                                                               | 示例                                                                                         |
| ------------- | -------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------- |
| `name`        | 中文，2-8 个字，概括该 Controller 管理的业务范围。使用名词短语，结尾不加「管理」「模块」等冗余词。 | `"用户管理"`、`"登录管理"`、`"用户地址管理"`、`"关注管理"`、`"提醒规则"`、`"基金档案与净值"` |
| `description` | 中文，可选。一句话说明该分组包含哪些接口，面向什么场景。                                           | `"有关用户个人的一系列controller"`、`"收货地址 CRUD 与默认地址"`、`"有关登陆，注册"`         |

### 实际示例

```java
// ✅ 推荐
@Tag(name = "用户管理", description = "有关用户个人的一系列controller")
@Tag(name = "用户地址管理", description = "收货地址 CRUD 与默认地址")
@Tag(name = "登录管理", description = "有关登陆，注册")
@Tag(name = "关注管理")
@Tag(name = "提醒规则")
@Tag(name = "基金档案与净值")
@Tag(name = "区间总结")
@Tag(name = "后台用户管理")
@Tag(name = "焦点项目管理")
@Tag(name = "后台角色管理")

// ❌ 不推荐
@Tag(name = "用户管理模块")          // 多余「模块」
@Tag(name = "UserManagement")       // 面向中国人用中文
@Tag(name = "用户")                 // 太简短，不够具体
@Tag(name = "用户管理控制器")        // 多余「控制器」
```

### 命名要点

- 一个 Controller 一个 `@Tag`，不重复、不重叠。
- name 能独立表达分组含义，description 是补充说明，非必填。
- 如果 name 已经足够清晰（如「关注管理」「提醒规则」），description 可省略。

## @Operation 命名规范

### 位置

Controller 方法上，描述单个 API 接口的用途。

### 格式

```java
@Operation(summary = "{接口摘要}", description = "{接口描述}")
```

### 规则

| 属性          | 规则                                                            | 示例                                                             |
| ------------- | --------------------------------------------------------------- | ---------------------------------------------------------------- |
| `summary`     | 中文，必填。2-20 字，简洁概括接口功能。使用动词短语或动宾结构。 | `"获取用户信息"`、`"新增地址"`、`"搜索基金（按代码或名称模糊）"` |
| `description` | 中文，可选。补充说明接口的业务逻辑、触发条件、自动操作等。      | `"邮箱验证码登录自动注册"`、`"取消关注（级联删除提醒规则）"`     |

### 实际示例

```java
// ✅ 推荐
@Operation(summary = "获取用户信息")
@Operation(summary = "地址列表")
@Operation(summary = "新增地址")
@Operation(summary = "更新地址")
@Operation(summary = "删除地址")
@Operation(summary = "设为默认地址")
@Operation(summary = "登陆注册-验证码", description = "邮箱验证码登录自动注册")
@Operation(summary = "登陆-密码", description = "通过密码登陆")
@Operation(summary = "验证码发送", description = "验证码发送接口")
@Operation(summary = "重置密码", description = "通过邮箱验证码重置密码")
@Operation(summary = "登出", description = "当前 token 加入黑名单")
@Operation(summary = "我的关注列表（含最新净值）")
@Operation(summary = "取消关注（级联删除提醒规则）")
@Operation(summary = "搜索基金（按代码或名称模糊）")
@Operation(summary = "手动触发生成总结")
@Operation(summary = "用户分页列表")
@Operation(summary = "封禁/解封用户")
@Operation(summary = "修改用户角色")

// ❌ 不推荐
@Operation(summary = "getUserInfo")              // 面向中国人用中文
@Operation(summary = "获取用户信息接口")           // 多余「接口」
@Operation(summary = "用于获取当前登录用户的基本信息") // 太啰嗦，长描述放 description
@Operation(summary = "处理")                      // 无意义
```

### 命名要点

- summary 使用中文，面向 API 文档的读者（前端、测试、产品）。
- summary 格式：`"动词 + 宾语"` 或 `"场景 - 子场景"`。
  - 动词 + 宾语：`"获取用户信息"`、`"新增地址"`。
  - 场景 - 子场景：`"登陆注册-验证码"`、`"登陆-密码"`。
- description 写「额外信息」：自动注册、级联操作、触发条件、注意事项等。
- summary 中括号内容用于补充说明条件，例如 `"（含最新净值）"`、`"（按代码或名称模糊）"`。

## @Schema 命名规范

### 位置

- 类级别：DTO、VO、Entity、Query 类上，描述该数据结构的业务含义。
- 字段级别：类中字段上，描述该字段的含义。

### 类级别格式

```java
@Schema(name = "{数据结构名称}", description = "{补充说明}")
```

### 类级别规则

| 属性          | 规则                                                                                                                    | 示例                                                                |
| ------------- | ----------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------- |
| `name`        | 中文，必填。格式：`"{业务名}{类型后缀}"`。类型后缀：DTO 用「入参」或「DTO」，VO 用「出参」或「VO」，Entity 用「对象」。 | `"新增收货地址DTO"`、`"用户信息VO"`、`"总结出参"`、`"基金档案出参"` |
| `description` | 中文，可选。Entity 类建议填写对应的数据库表名。                                                                         | `"用户收货地址表"`、`"用户表"`                                      |

### 字段级别格式

```java
@Schema(description = "{字段含义}", example = "{示例值}")
```

### 字段级别规则

| 属性          | 规则                                                         | 示例                                                                               |
| ------------- | ------------------------------------------------------------ | ---------------------------------------------------------------------------------- |
| `description` | 中文，必填。描述该字段的业务含义。枚举字段必须写明取值含义。 | `"用户ID"`、`"收件人姓名"`、`"是否默认地址：1=是，0=否"`、`"状态：0=正常，1=封禁"` |
| `example`     | 可选。提供示例值帮助理解。                                   | `example = "1693676136@qq.com"`、`example = "小明"`                                |

### 分类示例

#### DTO 类级别

```java
// ✅ 推荐
@Schema(name = "新增收货地址DTO")
@Schema(name = "更新收货地址DTO")
@Schema(name = "邮箱验证码登录DTO")
@Schema(name = "密码登陆DTO")
@Schema(name = "修改密码DTO")
@Schema(name = "重置密码DTO")
@Schema(name = "换绑邮箱DTO")
@Schema(name = "用户资料更新DTO")
@Schema(name = "邮箱DTO", description = "注册用户所需要的信息")
@Schema(name = "添加关注基金入参")
@Schema(name = "更新关注备注入参")
@Schema(name = "新增提醒规则入参")
@Schema(name = "更新提醒规则入参")
@Schema(name = "手动触发总结生成入参")
@Schema(name = "总结分页查询入参")
@Schema(name = "净值历史分页查询入参")

// ❌ 不推荐
@Schema(name = "AddressCreateDTO")          // 面向中国人用中文
@Schema(name = "新增地址")                  // 缺少类型后缀，无法区分是 DTO 还是 VO
@Schema(name = "新增收货地址的请求参数")      // 太啰嗦
```

#### VO 类级别

```java
// ✅ 推荐
@Schema(name = "用户信息VO")
@Schema(name = "收货地址VO")
@Schema(name = "基金档案出参")
@Schema(name = "基金净值出参")
@Schema(name = "提醒规则出参")
@Schema(name = "我的关注出参")
@Schema(name = "总结出参")
@Schema(name = "后台用户管理VO")
@Schema(name = "后台角色管理VO")

// ❌ 不推荐
@Schema(name = "UserInfoVO")                // 面向中国人用中文
@Schema(name = "用户信息")                  // 缺少类型后缀
@Schema(name = "用户信息出参VO")             // 类型后缀冗余，出参和VO选一个
```

#### Entity 类级别

```java
// ✅ 推荐
@Schema(name = "UserAddressDO对象", description = "用户收货地址表")
@Schema(name = "UserDO对象", description = "用户表")
@Schema(name = "SpotlightDO对象", description = "首页焦点展示项目表")

// ✅ 字段级别
@Schema(description = "用户ID")
@Schema(description = "邮箱", example = "qq.com")
@Schema(description = "是否默认地址")
@Schema(description = "是否默认地址：1=是，0=否")
@Schema(description = "状态：0=正常，1=封禁")
@Schema(description = "密码（加密存储）")
```

### 命名要点

- 类级别 `name` 使用中文，让 Swagger/Knife4j 文档中的 Schema 名称可读。
- DTO 用 `"{业务动作}{业务名}DTO"` 或 `"{业务名}入参"` 格式。
- VO 用 `"{业务名}VO"` 或 `"{业务名}出参"` 格式。
- 同一模块内风格统一：要么都用 `DTO`/`VO` 后缀，要么都用「入参」/「出参」后缀。
- 字段级别 `description` 必须写且有意义；枚举字段必须写明取值和含义。
- `example` 不是必填，但邮件、手机号等格式要求高的字段建议提供。

## @RequestMapping 路径命名规范

### 格式

```
/api/{模块名}/{资源名(复数)}
```

### 规则

| 组成部分 | 规则                       | 示例                                        |
| -------- | -------------------------- | ------------------------------------------- |
| 前缀     | `/api`，全局固定           | `/api/...`                                  |
| 模块名   | 小写英文，标识业务模块     | `/api/user`、`/api/caishen`、`/api/admin`   |
| 资源名   | 小写英文复数，RESTful 风格 | `/api/user/addresses`、`/api/caishen/funds` |

### 实际示例

```java
// ✅ 推荐
@RequestMapping("/api/user")             // UserController
@RequestMapping("/api/user/addresses")   // UserAddressController
@RequestMapping("/api/auth")             // LoginController（认证类可用 auth）
@RequestMapping("/api/caishen/funds")    // FundController
@RequestMapping("/api/caishen/watches")  // WatchController
@RequestMapping("/api/caishen/summaries")// SummaryController
@RequestMapping("/api/caishen")          // AlertController（共用前缀）
@RequestMapping("/api/admin/user-do")    // UserAdminController
@RequestMapping("/api/admin/spotlight")  // SpotlightController
@RequestMapping("/api/admin/role-do")    // RoleAdminController

// ❌ 不推荐
@RequestMapping("/api/User")              // 不使用大写
@RequestMapping("/api/user_address")      // 不使用下划线
@RequestMapping("/api/user/getUserInfo")  // 动作放 HTTP Method，不放路径
```

### 子路径命名

方法级的 `@GetMapping`、`@PostMapping` 等子路径：

```java
// ✅ 推荐
@GetMapping              // 列表，路径 = 类路径
@GetMapping("/{id}")     // 详情
@PostMapping             // 新增
@PutMapping("/{id}")     // 更新
@DeleteMapping("/{id}")  // 删除
@PutMapping("/{id}/default")  // 特殊操作
@PostMapping("/login-email")  // 登录
@PostMapping("/send-code")    // 发送验证码
@PostMapping("/password/reset") // 密码重置

// ❌ 不推荐
@GetMapping("/getList")          // 动词放路径
@PostMapping("/createAddress")   // 动词放路径
@GetMapping("/findById")         // 动词放路径
```

## @ApiResponse 与 @Parameter 命名规范

### @ApiResponse

OWL 当前未使用 `@ApiResponse` 的 `description` 属性。如后续引入：

```java
@ApiResponse(responseCode = "200", description = "操作成功")
@ApiResponse(responseCode = "400", description = "参数校验失败")
@ApiResponse(responseCode = "401", description = "未登录或 Token 过期")
@ApiResponse(responseCode = "403", description = "无权限")
```

- `description` 使用中文，简洁说明该状态码的含义。
- 仅标注非 200 的响应码，200 由全局配置处理。

### @Parameter

OWL 当前未使用 `@Parameter`。如后续引入：

```java
@Parameter(description = "地址ID", example = "1")
@Parameter(description = "搜索关键词", example = "易方达")
```

- `description` 使用中文，描述参数含义。
- `example` 提供有意义的示例值。

## 禁止事项

1. 禁止在 `@Tag(name)`、`@Operation(summary)`、`@Schema(name)` 中使用英文。
2. 禁止 `@Operation(summary)` 为空或写「接口」「处理」「操作」等无意义文字。
3. 禁止 `@Schema(description)` 为空；字段必须有描述。
4. 禁止在 `@Schema(name)` 中只写英文类名，不加中文翻译。
5. 禁止 URL 路径使用大写字母、下划线或动词。
6. 禁止 `@Tag(name)` 在不同 Controller 中重复。
7. 禁止在 `description` 中写实现细节（如「调用 XxxService.xxx 方法」），只写业务含义。

## 变更历史

- 2026-08-27：创建元数据注解命名规范。

## 相关文档

- [OWL 文档中心](../../README.md)
- [OWL 文档编写规范](../documentation-standard.md)
- [代码文件与包名命名规范](code-file-naming-convention.md)
- [代码标识符命名规范](code-identifier-naming-convention.md)
- [OWL 代码注释规范](../code-comment-standard.md)
