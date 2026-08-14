# OWL 前端联调接口文档

| 属性 | 值 |
| --- | --- |
| 状态 | 草稿 |
| 负责人 | 仓库 Owner |
| 创建时间 | 2026-08-14 |
| 更新时间 | 2026-08-14 |
| 适用版本 | main 分支源码快照 |
| 生成方式 | 源码静态整理，非工具自动生成 |

## 背景与目标

本文档面向前端联调人员，说明 OWL 后端当前暴露的 REST 接口、鉴权方式、统一响应结构、请求与响应字段，以及联调时已知的问题。

接口事实以源码为准，交互式文档入口为 Knife4j `/doc.html`，OpenAPI 地址为 `/v3/api-docs`。所有接口均由 `WebMvcConfig` 统一加上 `/api` 前缀。

## 通用约定

### 基础地址

| 项目 | 值 |
| --- | --- |
| 服务根路径 | `/api` |
| 本地默认端口 | 8080，最终以 Nacos 或部署配置为准 |
| 内容类型 | JSON，UTF-8；文件上传使用 `multipart/form-data` |
| 跨域 | 已开启全域名跨域，允许携带 `Authorization` |

### 鉴权

登录成功后，`Result.data` 返回 JWT。后续请求在请求头携带：

```text
Authorization: Bearer <token>
```

安全规则见 `common/.../security/config/SecurityConfig.java`：

| 规则 | 路径 |
| --- | --- |
| 公开 | `/api/auth/send-code`、`/api/auth/login-email`、`/api/auth/login-password`、`/api/auth/password/reset` |
| 公开 | `/api/admin/feature/**`、`/api/admin/spotlight/**` |
| 公开 | `/api/public/**`、Swagger/Knife4j 相关路径 |
| 需要登录 | 其余接口 |
| 需要 ADMIN | `/api/admin/**`（不含上述公开路径） |

角色名来自登录用户的角色表，支持 `ADMIN`、`MERCHANT`、`USER`。除后台管理外，当前未对消费者/商家接口做角色隔离。

未登录或 Token 无效时，HTTP 状态为 401，响应体仍为 `Result`；无权限时 HTTP 状态为 403。

### 统一响应

成功响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

分页响应：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "currentPage": 1,
    "pageSize": 10,
    "total": 100,
    "totalPage": 10,
    "records": []
  }
}
```

部分接口成功时 `data` 为 null，部分接口会返回状态枚举（如 `SUCCESS`、`CREATED`）。前端统一以 `code` 判断请求结果，不依赖 `data`。

### 状态码

| code | 说明 |
| --- | --- |
| 200 | 操作成功 |
| 201 | 创建成功 |
| 400 | 请求参数错误 |
| 401 | 暂未登录或 Token 已过期 |
| 403 | 没有相关权限 |
| 404 | 资源不存在 |
| 405 | 请求方法不支持 |
| 500 | 服务器内部错误 |
| 9000 | 业务处理失败 |
| 9001 | 参数校验失败 |
| 9002 | 数据不存在 |
| 9003 | 数据已存在 |
| 9101 | 库存不足 |
| 9201 | 订单状态异常 |
| 9301 | 支付失败 |
| 9401 | 登录角色异常 |
| 9402 | 旧密码不正确 |
| 9403 | 邮箱已被使用 |
| 9404 | 仅支持 jpeg/png/webp 图片 |
| 9405 | 图片大小不能超过 5MB |
| 9501 | 验证码错误 |
| 9502 | 验证码错误次数过多 |
| 9503 | 验证码发送过于频繁 |
| 401001 | 登录已过期 |
| 401002 | Token 无效 |
| 401003 | 账号已被禁用 |
| 401004 | 账号未设置密码，请使用验证码登录 |
| 9995 | 业务异常 |
| 9997 | 功能尚未实现 |
| 9999 | 操作失败 |

## 用户中心

### 接口总览

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/auth/send-code` | 公开 | `SendCodeDTO` | null |
| POST | `/api/auth/login-email` | 公开 | `EmailLoginDTO` | String token |
| POST | `/api/auth/login-password` | 公开 | `PasswordLoginDTO` | String token |
| POST | `/api/auth/password/reset` | 公开 | `ResetPasswordDTO` | null |
| POST | `/api/auth/logout` | 登录 | `Authorization` 头 | null |
| GET | `/api/user/info` | 登录 | - | `UserInfoVO` |
| PUT | `/api/user/avatar` | 登录 | multipart，字段 `file` | String avatarUrl |
| PUT | `/api/user/info` | 登录 | `UserInfoUpdateDTO` | null |
| PUT | `/api/user/email` | 登录 | `EmailBindDTO` | null |
| PUT | `/api/user/password` | 登录 | `PasswordUpdateDTO` | null |
| GET | `/api/user/addresses` | 登录 | - | `AddressVO[]` |
| POST | `/api/user/addresses` | 登录 | `AddressCreateDTO` | Long 地址ID |
| GET | `/api/user/addresses/{id}` | 登录 | 路径 `id` | `AddressVO` |
| PUT | `/api/user/addresses/{id}` | 登录 | 路径 `id` + `AddressUpdateDTO` | null |
| DELETE | `/api/user/addresses/{id}` | 登录 | 路径 `id` | null |
| PUT | `/api/user/addresses/{id}/default` | 登录 | 路径 `id` | null |

### 请求模型

| DTO | 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- |
| `SendCodeDTO` | email | string | 是 | 邮箱地址 |
| `EmailLoginDTO` | email | string | 是 | 邮箱地址，未注册邮箱登录时自动注册 |
| `EmailLoginDTO` | code | string | 是 | 6 位数字验证码 |
| `PasswordLoginDTO` | email | string | 是 | 邮箱地址 |
| `PasswordLoginDTO` | password | string | 是 | 登录密码 |
| `ResetPasswordDTO` | email | string | 是 | 邮箱地址 |
| `ResetPasswordDTO` | code | string | 是 | 6 位数字验证码 |
| `ResetPasswordDTO` | newPassword | string | 是 | 新密码，8-64 位，需同时包含字母和数字 |
| `UserInfoUpdateDTO` | userName | string | 否 | 用户名，最大 50 字符 |
| `UserInfoUpdateDTO` | nickname | string | 否 | 昵称，最大 50 字符 |
| `UserInfoUpdateDTO` | phone | string | 否 | 手机号，需符合手机号格式 |
| `UserInfoUpdateDTO` | remark | string | 否 | 备注，最大 255 字符 |
| `EmailBindDTO` | newEmail | string | 是 | 新邮箱地址 |
| `EmailBindDTO` | code | string | 是 | 发送到新邮箱的验证码 |
| `PasswordUpdateDTO` | oldPassword | string | 否 | 旧密码，账号未设置密码时可省略 |
| `PasswordUpdateDTO` | newPassword | string | 是 | 新密码，规则同重置密码 |
| `AddressCreateDTO` | receiverName | string | 是 | 收件人姓名，最大 50 字符 |
| `AddressCreateDTO` | receiverPhone | string | 是 | 收件人电话 |
| `AddressCreateDTO` | province | string | 是 | 省 |
| `AddressCreateDTO` | city | string | 是 | 市 |
| `AddressCreateDTO` | district | string | 是 | 区/县 |
| `AddressCreateDTO` | detail | string | 是 | 详细地址，最大 255 字符 |
| `AddressCreateDTO` | isDefault | boolean | 否 | 是否默认地址 |
| `AddressUpdateDTO` | 同 `AddressCreateDTO` | - | 是 | 更新地址时字段同上 |

### 响应模型

`UserInfoVO`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 用户 ID |
| userCode | string | 账号编号 |
| userName | string | 用户名 |
| nickname | string | 昵称 |
| email | string | 邮箱 |
| phone | string | 手机号 |
| remark | string | 备注 |
| role | string | 角色名 |
| avatarUrl | string | 头像 URL |
| createTime | string | 创建时间 |

`AddressVO`：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| id | Long | 地址 ID |
| receiverName | string | 收件人姓名 |
| receiverPhone | string | 收件人电话 |
| province | string | 省 |
| city | string | 市 |
| district | string | 区/县 |
| detail | string | 详细地址 |
| isDefault | integer | 1=是，0=否 |
| createTime | string | 创建时间 |
| updateTime | string | 更新时间 |

### 示例

发送验证码：

```bash
curl -X POST http://localhost:8080/api/auth/send-code \
  -H "Content-Type: application/json" \
  -d '{"email":"you@example.com"}'
```

邮箱验证码登录：

```bash
curl -X POST http://localhost:8080/api/auth/login-email \
  -H "Content-Type: application/json" \
  -d '{"email":"you@example.com","code":"123456"}'
```

```json
{
  "code": 200,
  "message": "操作成功",
  "data": "<JWT>"
}
```

## AI 对话

### 接口总览

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/ai/chat` | 登录 | `ChatRequestDTO` | String 回复内容 |
| POST | `/api/ai/chat/stream` | 登录 | `ChatRequestDTO` | SSE，`text/event-stream` |
| POST | `/api/ai/conversation` | 登录 | `CreateConversationDTO`，可省略 body | String 会话 ID |
| GET | `/api/ai/conversation/list` | 登录 | - | `ConversationVO[]` |
| GET | `/api/ai/conversation/{id}/messages` | 登录 | 路径 `id` | `MessageVO[]` |
| DELETE | `/api/ai/conversation/{id}` | 登录 | 路径 `id` | null |

### 请求与响应模型

| DTO/VO | 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- |
| `ChatRequestDTO` | conversationId | string | 是 | 会话 ID |
| `ChatRequestDTO` | message | string | 是 | 用户消息 |
| `CreateConversationDTO` | title | string | 否 | 会话标题，可自动生成 |
| `ConversationVO` | id | string | - | 会话 ID |
| `ConversationVO` | title | string | - | 会话标题 |
| `ConversationVO` | createTime | string | - | 创建时间 |
| `ConversationVO` | updateTime | string | - | 更新时间 |
| `MessageVO` | id | Long | - | 消息 ID |
| `MessageVO` | role | string | - | `user` / `assistant` / `system` |
| `MessageVO` | content | string | - | 消息内容 |
| `MessageVO` | createTime | string | - | 消息创建时间 |

流式接口按 SSE 解析，后端 `produces` 为 `text/event-stream`，每个事件为字符串回复片段。

## 电商中心

### 消费者商品

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| GET | `/api/pitaya/product/consumer/products` | 登录 | query：`productName`、`pageNum`、`pageSize` | `ResultPage<ProductVO>` |
| GET | `/api/pitaya/product/consumer/category` | 登录 | - | `CategoryVO[]` |
| GET | `/api/pitaya/product/consumer/detail` | 登录 | query：`productId` | `ProductDetailVO` |

`productName` 为必填参数，可传空字符串进行全量分页查询。`pageSize` 超过 50 会被后端限制为 50。

| VO | 字段 | 类型 | 说明 |
| --- | --- | --- | --- |
| `ProductVO` | productName | string | 商品名 |
| `ProductVO` | categoryName | string | 分类名 |
| `ProductVO` | price | number | 单价 |
| `ProductVO` | stock | integer | 库存 |
| `ProductVO` | status | integer | 1=启用，0=禁用 |
| `ProductVO` | createTime | string | 创建时间 |
| `ProductVO` | updateTime | string | 更新时间 |
| `ProductDetailVO` | id | Long | 商品 ID |
| `ProductDetailVO` | categoryId | Long | 分类 ID |
| `ProductDetailVO` | categoryName | string | 分类名 |
| `ProductDetailVO` | sellerId | Long | 商家 ID |
| `ProductDetailVO` | name | string | 商品名 |
| `ProductDetailVO` | description | string | 商品描述 |
| `ProductDetailVO` | price | number | 单价 |
| `ProductDetailVO` | stock | integer | 库存 |
| `ProductDetailVO` | coverImg | string | 封面图 |
| `ProductDetailVO` | images | array | `imageUrl`、`imageSort` |
| `ProductDetailVO` | createTime | string | 创建时间 |
| `ProductDetailVO` | updateTime | string | 更新时间 |
| `CategoryVO` | parentId | Long | 父分类 ID，顶级为 0 |
| `CategoryVO` | name | string | 分类名 |
| `CategoryVO` | level | integer | 分类层级 |
| `CategoryVO` | sort | integer | 排序 |
| `CategoryVO` | description | string | 分类描述 |

### 消费者购物车

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/pitaya/cart/consumer/product` | 登录 | `ShoppingCartDTO` | null |
| DELETE | `/api/pitaya/cart/consumer/product` | 登录 | query：`userId`、`productId` | null |
| PUT | `/api/pitaya/cart/consumer/product` | 登录 | `ShoppingCartDTO` | null |
| GET | `/api/pitaya/cart/consumer/carts` | 登录 | query：`pageNum`、`pageSize` | `ResultPage<ShoppingCartVO>` |

| DTO/VO | 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- |
| `ShoppingCartDTO` | userId | Long | 是 | 用户 ID，新增/更新时来自请求体 |
| `ShoppingCartDTO` | productId | Long | 是 | 商品 ID |
| `ShoppingCartDTO` | quantity | integer | 否 | 数量 |
| `ShoppingCartDTO` | checked | integer | 否 | 0=未勾选，1=勾选 |
| `ShoppingCartVO` | cartId | Long | - | 购物车 ID |
| `ShoppingCartVO` | productId | Long | - | 商品 ID |
| `ShoppingCartVO` | quantity | integer | - | 数量 |
| `ShoppingCartVO` | checked | integer | - | 0=未勾选，1=勾选 |
| `ShoppingCartVO` | productName | string | - | 商品名 |
| `ShoppingCartVO` | price | number | - | 单价 |
| `ShoppingCartVO` | coverImage | string | - | 封面图 |

联调注意：购物车列表使用登录态用户 ID，但新增、更新使用请求体里的 `userId`，删除使用 query 里的 `userId`，当前实现不统一。

### 消费者订单

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/pitaya/order/consumer` | 登录 | `OrderDTO` | null/状态枚举 |
| PUT | `/api/pitaya/order/consumer/{orderId}/confirmed` | 登录 | 路径 `orderId` | null/状态枚举 |
| PUT | `/api/pitaya/order/consumer/{orderId}/cancel` | 登录 | 路径 `orderId` | null/状态枚举 |
| GET | `/api/pitaya/order/consumer/{orderId}` | 登录 | 路径 `orderId` | `OrderDetailVO` |
| GET | `/api/pitaya/order/consumer/orders` | 登录 | query：`pageNum`、`pageSize` | `ResultPage<OrderListVO>` |

`OrderDTO`：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| source | string | 否 | 下单来源，`CART` / `BUY_NOW` |
| remark | string | 否 | 订单备注 |
| addressId | Long | 是 | 收货地址 ID |
| items | array | 是 | 订单商品，元素为 `productId`、`quantity` |

响应模型：

| VO | 字段 | 类型 | 说明 |
| --- | --- | --- | --- |
| `OrderDetailVO` | id | Long | 订单 ID |
| `OrderDetailVO` | orderNo | string | 订单编号 |
| `OrderDetailVO` | orderStatus | integer | 0=待支付，1=待发货，2=待收货，3=已完成，4=取消 |
| `OrderDetailVO` | payStatus | integer | 支付状态 |
| `OrderDetailVO` | totalAmount | number | 订单总金额 |
| `OrderDetailVO` | createTime | string | 下单时间 |
| `OrderDetailVO` | payTime | string | 支付时间 |
| `OrderDetailVO` | deliveryTime | string | 发货时间 |
| `OrderDetailVO` | finishTime | string | 完成时间 |
| `OrderDetailVO` | address | object | 收货地址快照，结构同 `AddressVO` |
| `OrderDetailVO` | items | array | 订单商品列表 |
| `OrderItemVO` | productId | Long | 商品 ID |
| `OrderItemVO` | productName | string | 商品名 |
| `OrderItemVO` | productImage | string | 商品图片 |
| `OrderItemVO` | unitPrice | number | 单价 |
| `OrderItemVO` | quantity | integer | 数量 |
| `OrderItemVO` | totalPrice | number | 小计 |
| `OrderListVO` | orderId | Long | 订单 ID |
| `OrderListVO` | orderNo | string | 订单编号 |
| `OrderListVO` | orderStatus | integer | 状态说明同上 |
| `OrderListVO` | totalAmount | number | 总金额 |
| `OrderListVO` | itemCount | integer | 商品数量 |
| `OrderListVO` | createTime | string | 下单时间 |

消费者订单的用户均取自登录态 Token，地址 ID 必须属于当前用户。

### 商家商品

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/pitaya/product/merchant/products` | 登录 | `ProductDTO` | null/状态枚举 |
| PUT | `/api/pitaya/product/merchant/products/modify` | 登录 | `ProductDTO` | null/状态枚举 |
| DELETE | `/api/pitaya/product/merchant/products/{productId}` | 登录 | 路径 `productId` | null/状态枚举 |
| PUT | `/api/pitaya/product/merchant/products/{productId}/status` | 登录 | 路径 `productId` + query `productStatus` | null/状态枚举 |

`ProductDTO`：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| id | Long | 是 | 商品 ID，更新时必传 |
| categoryId | Long | 是 | 分类 ID |
| sellerId | Long | 否 | 商家 ID |
| productName | string | 否 | 商品名 |
| description | string | 否 | 商品描述 |
| price | number | 是 | 价格 |
| stock | integer | 是 | 库存 |
| coverImage | string | 否 | 封面图 URL |
| status | integer | 否 | 1=启用，0=禁用 |
| images | array | 是 | 至少一张，元素为 `productId`、`imageUrl`、`sort` |

### 商家订单

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| GET | `/api/pitaya/order/merchant/orders` | 登录 | query：`pageNum`、`pageSize`、`userId` | `ResultPage<OrderListVO>` |
| PUT | `/api/pitaya/order/merchant` | 登录 | query：`orderId`、`orderStatus` | null |

商家订单列表的商家身份取自登录态 Token，`userId` 参数表示被查询的消费者用户 ID。`orderStatus` 取值同消费者订单状态说明。

## 价格中心

### 物品与分类

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/item/page` | 登录 | `ItemIntroDTO` | `ResultPage<PriceItemVO>` |
| DELETE | `/api/item/{id}` | 登录 | 路径 `id` | 空实现 |
| GET | `/api/category/tree` | 登录 | - | 空实现，返回 null |
| DELETE | `/api/category/{id}` | 登录 | 路径 `id` | 空实现 |
| DELETE | `/api/source/{id}` | 登录 | 路径 `id` | 空实现 |

`ItemIntroDTO` 字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| itemName | string | 是 | 物品名 |
| pageNum | integer | 否 | 页码，默认 1 |
| pageSize | integer | 否 | 每页条数，默认 10 |

`PriceItemVO` 字段：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| itemId | Long | 物品 ID |
| itemName | string | 物品名 |
| unit | string | 计量单位 |
| specification | string | 规格描述 |
| categoryName | string | 分类名 |

`/api/location`、`/api/price-item-media-do` 控制器当前为空，无可用接口。

### 价格查询

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| POST | `/api/price/latest` | 登录 | `PriceLatestQueryDTO` | `PriceLatestVO` |
| POST | `/api/price/trend` | 登录 | `PriceTrendQueryDTO` | `PriceTrendVO[]` |
| POST | `/api/price/compare/location` | 登录 | `PriceCompareLocationDTO` | `PriceCompareVO` |
| POST | `/api/price/compare/source` | 登录 | `PriceCompareSourceDTO` | `SourceCompareVO[]` |

公共查询参数（`BasePriceQueryDTO`）：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| itemId | Long | 否 | 单个物品 ID，需为正整数 |
| itemCode | string | 否 | 物品编码，最大 64 字符 |
| itemIds | array | 否 | 多个物品 ID，最多 100 个 |
| locationId | Long | 否 | 单个地点 ID |
| locationIds | array | 否 | 多个地点 ID，最多 50 个 |
| sourceIds | array | 否 | 来源 ID 列表，最多 20 个 |
| minReliability | integer | 否 | 最小可靠等级，1-5 |
| currency | string | 否 | 币种，支持 CNY、USD、EUR、JPY、GBP、HKD |
| minConfidence | number | 否 | 最小可信度，0-100 |

`PriceTrendQueryDTO` 额外字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| startTime | string | 是 | 开始时间，如 `2026-01-01T00:00:00` |
| endTime | string | 是 | 结束时间 |
| granularity | string | 是 | 聚合粒度：`HOUR`、`DAY`、`WEEK`、`MONTH`、`YEAR` |

`PriceCompareLocationDTO`、`PriceCompareSourceDTO` 额外字段：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| targetTime | string | 是 | 对比目标时间，如 `2026-06-05T10:30:00` |

响应模型：

| VO | 字段 | 类型 | 说明 |
| --- | --- | --- | --- |
| `PriceLatestVO` | 继承 `PriceItemVO` | - | 物品基本信息 |
| `PriceLatestVO` | locationName | string | 地点名 |
| `PriceLatestVO` | price | number | 价格 |
| `PriceLatestVO` | currency | string | 币种 |
| `PriceLatestVO` | priceUnit | string | 价格单位 |
| `PriceLatestVO` | sourceName | string | 来源名 |
| `PriceLatestVO` | reliabilityLevel | integer | 可靠等级 1-5 |
| `PriceLatestVO` | effectiveTime | string | 生效时间 |
| `PriceLatestVO` | confidence | number | 可信度 |
| `PriceTrendVO` | 继承 `PriceItemVO` | - | 物品基本信息 |
| `PriceTrendVO` | locationId | Long | 地点 ID |
| `PriceTrendVO` | locationName | string | 地点名 |
| `PriceTrendVO` | trend | array | `time`、`price` 数据点 |
| `PriceCompareVO` | item | object | 物品信息 |
| `PriceCompareVO` | prices | array | `locationName`、`price` |
| `SourceCompareVO` | sourceName | string | 来源名 |
| `SourceCompareVO` | price | number | 价格 |
| `SourceCompareVO` | reliabilityLevel | integer | 可靠等级 |
| `SourceCompareVO` | confidence | number | 可信度 |

## 后台管理

### 特性管理

该模块路径已配置为公开访问。

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| GET | `/api/admin/feature` | 公开 | - | `FeatureVO[]` |
| GET | `/api/admin/feature/{id}` | 公开 | 路径 `id` | `FeatureVO` |
| POST | `/api/admin/feature` | 公开 | `FeatureDTO` | Integer |
| PUT | `/api/admin/feature/{id}` | 公开 | 路径 `id` + `FeatureDTO` | null |
| DELETE | `/api/admin/feature/{id}` | 公开 | 路径 `id` | null |

| DTO/VO | 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- |
| `FeatureDTO` | id | Long | 新增否，修改是 | 主键 |
| `FeatureDTO` | icon | string | 是 | 图标标识 |
| `FeatureDTO` | title | string | 是 | 标题 |
| `FeatureDTO` | description | string | 否 | 描述 |
| `FeatureDTO` | sortOrder | integer | 是 | 排序，值越小越靠前 |
| `FeatureVO` | 字段同 `FeatureDTO` | - | - | 列表返回 |

### 焦点项目管理

该模块路径已配置为公开访问。

| 方法 | 路径 | 鉴权 | 请求 | 响应 data |
| --- | --- | --- | --- | --- |
| GET | `/api/admin/spotlight` | 公开 | - | `SpotlightVO[]` |
| GET | `/api/admin/spotlight/{id}` | 公开 | 路径 `id` | `SpotlightVO` |
| POST | `/api/admin/spotlight` | 公开 | `multipart/form-data`，字段见 `SpotlightDTO` | Integer |
| PUT | `/api/admin/spotlight/{id}` | 公开 | 路径 `id` + JSON `SpotlightDTO` | null |
| DELETE | `/api/admin/spotlight/{id}` | 公开 | 路径 `id` | null |

| DTO/VO | 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- | --- |
| `SpotlightDTO` | id | Long | 新增否，修改是 | 主键 |
| `SpotlightDTO` | eyebrow | string | 是 | 眉题，最大 50 字符 |
| `SpotlightDTO` | title | string | 是 | 主标题，最大 100 字符 |
| `SpotlightDTO` | description | string | 否 | 描述，最大 500 字符 |
| `SpotlightDTO` | image | file | 是 | 图片文件，新增必传 |
| `SpotlightDTO` | sortOrder | integer | 是 | 排序，0-9999 |
| `SpotlightDTO` | link | string | 否 | 跳转链接，最大 255 字符 |
| `SpotlightDTO` | target | string | 否 | 打开方式，如 `_blank` |
| `SpotlightVO` | id | Long | - | 主键 |
| `SpotlightVO` | eyebrow | string | - | 眉题 |
| `SpotlightVO` | title | string | - | 主标题 |
| `SpotlightVO` | description | string | - | 描述 |
| `SpotlightVO` | imageUrl | string | - | 图片 URL |
| `SpotlightVO` | sortOrder | integer | - | 排序 |
| `SpotlightVO` | link | string | - | 跳转链接 |
| `SpotlightVO` | target | string | - | 打开方式 |

## 已知问题与联调注意

- 焦点项目更新接口 `PUT /api/admin/spotlight/{id}` 当前使用 `@RequestBody` 接收包含 `MultipartFile` 的 DTO，JSON 无法直接绑定文件，联调前需要与后端确认是否改回 `multipart/form-data`。
- 购物车新增、更新、删除使用请求传入的用户 ID，购物车列表使用登录态用户 ID，前后端需保持一致，建议后端后续统一为登录态用户。
- 消费者、商家商品与订单接口只要求登录，不强制 `MERCHANT` 角色；后台管理接口 `/api/admin/**` 才要求 `ADMIN`。
- 后台用户管理、角色管理、管理员日志、业务日志控制器目前只有类定义和待实现方法，没有可调用的接口。
- 价格中心分类树、物品删除、来源删除为空实现；`/api/location` 与 `/api/price-item-media-do` 控制器为空。
- 物品分页 `POST /api/item/page` 在查询结果为空时后端可能返回 9999，联调时需注意空数据场景。
- 商家商品 DTO 中 `id` 使用了 `@NotEmpty`，新增商品时是否需要传 `id` 需与后端确认；当前服务实现仍会执行插入。
- 订单列表接口的 `pageNum`、`pageSize` 参数未标注 `@RequestParam`，当前为可选参数，但漏传可能导致后端分页异常，联调时建议始终传值。
- 流式聊天接口返回 SSE，前端需使用事件流解析，不能按普通 JSON 解析。

## 源码索引

| 模块 | 控制器 |
| --- | --- |
| 用户中心 | `watermelon/user/src/main/java/.../controller/LoginController.java` |
| 用户中心 | `watermelon/user/src/main/java/.../controller/UserController.java` |
| 用户中心 | `watermelon/user/src/main/java/.../controller/UserAddressController.java` |
| AI 对话 | `watermelon/crow/src/main/java/.../controller/AiChatController.java` |
| AI 对话 | `watermelon/crow/src/main/java/.../controller/ConversationController.java` |
| 电商中心 | `watermelon/pitaya/src/main/java/.../consumer/controller/ProductController.java` |
| 电商中心 | `watermelon/pitaya/src/main/java/.../consumer/controller/ConCartController.java` |
| 电商中心 | `watermelon/pitaya/src/main/java/.../consumer/controller/ConOrderController.java` |
| 电商中心 | `watermelon/pitaya/src/main/java/.../merchant/controller/MerchantProductController.java` |
| 电商中心 | `watermelon/pitaya/src/main/java/.../merchant/controller/MerOrderController.java` |
| 价格中心 | `watermelon/sugarcane/src/main/java/.../controller/ItemController.java` |
| 价格中心 | `watermelon/sugarcane/src/main/java/.../controller/CategoryController.java` |
| 价格中心 | `watermelon/sugarcane/src/main/java/.../controller/RecordController.java` |
| 价格中心 | `watermelon/sugarcane/src/main/java/.../controller/SourceController.java` |
| 后台管理 | `watermelon/administration/src/main/java/.../controller/FeatureController.java` |
| 后台管理 | `watermelon/administration/src/main/java/.../controller/SpotlightController.java` |

## 变更历史

| 日期 | 变更 | 负责人 |
| --- | --- | --- |
| 2026-08-14 | 创建初版，整理 main 分支现有接口 | 仓库 Owner |
