# pitaya 模块：火龙果（电商）

## 定位

专注电商项目的建立，分为消费者端（consumer）和商家端（merchant）两大子域。

## 架构依赖

```
common → domain → api → pitaya
```

## 包结构

```
xyz.nanian.owl.pitaya
├── constant/
│   └── ShopConstant.java              ← 商城常量
├── consumer/                           ← 消费者端
│   ├── controller/
│   │   ├── ConCartController.java     ← 购物车接口
│   │   ├── ConOrderController.java    ← 订单接口（消费者）
│   │   └── ProductController.java     ← 商品浏览接口
│   ├── mapper/
│   │   ├── ConCartMapper.java
│   │   ├── ConOrderMapper.java
│   │   └── ProductMapper.java
│   └── service/
│       ├── ConCartService.java / ConCartServiceImpl.java
│       ├── ConOrderService.java / ConOrderServiceImpl.java
│       └── ProductService.java / ProductServiceImpl.java
├── merchant/                           ← 商家端
│   ├── controller/
│   │   ├── MerOrderController.java    ← 订单管理（商家）
│   │   └── MerchantProductController.java ← 商品管理（商家）
│   ├── mapper/
│   │   ├── MerOrderMapper.java
│   │   └── MerchantProductMapper.java
│   └── service/
│       ├── MerOrderService.java / MerOrderServiceImpl.java
│       └── MerchantProductService.java / MerchantProductServiceImpl.java
├── domain/
│   ├── dto/
│   │   ├── OrderDTO.java / OrderDetailDTO.java
│   │   ├── ProductDTO.java / ProductCategoryDTO.java
│   │   └── ShoppingCartDTO.java
│   ├── entity/
│   │   ├── CategoryDO.java           ← 商品分类
│   │   ├── ProductDO.java            ← 商品
│   │   ├── ProductImageDO.java       ← 商品图片
│   │   ├── OrderDO.java              ← 订单
│   │   ├── OrderDetailDO.java        ← 订单详情
│   │   ├── ShoppingCartDO.java       ← 购物车
│   │   ├── StatsDailyDO.java         ← 每日统计
│   │   └── UserAddressDO.java        ← 用户地址
│   ├── query/                         ← 查询条件对象
│   └── vo/                            ← 视图对象
├── mapstruct/
│   ├── OrderConvert.java
│   ├── ProductConvert.java
│   └── ShoppingCartConvert.java
└── plan/                              ← 规划中的 API
    ├── CartApi.java
    ├── MerchantOrderApi.java
    ├── MerchantProductApi.java
    ├── OrderApi.java
    └── ProductApi.java
```

## 功能范围

| 端 | 功能 |
|----|------|
| 消费者 | 商品浏览、购物车、下单 |
| 商家 | 商品管理、订单管理 |

## 预期使用技术

- Redis 缓存
- Docker 部署
- RocketMQ 消息队列

## 数据库表

`pitaya` 数据库包含：`product_category`、`user`、`role`、`user_role`、`orders`、`order_detail`、`stats_daily`、`product`

## MyBatis 生成器配置

`src/main/resources/generatorConfig.xml` — 自动生成 Entity、Mapper、XML
