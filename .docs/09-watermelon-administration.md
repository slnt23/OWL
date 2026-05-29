# administration 模块：后台管理

## 定位

全局管理模块，提供后台管理功能，管理用户、角色、业务日志、焦点/特性数据等。

## 包结构

```
xyz.nanian.owl.admin
├── controller/
│   ├── UserAdminController.java      ← 用户管理
│   ├── RoleAdminController.java      ← 角色管理
│   ├── BizLogAdminController.java    ← 业务日志管理
│   ├── LogAdminController.java       ← 系统日志管理
│   ├── FeatureController.java        ← 特性数据管理
│   └── SpotlightController.java      ← 焦点数据管理
├── convert/
│   ├── FeatureConvert.java           ← 特性对象转换 (MapStruct)
│   └── SpotlightConvert.java         ← 焦点对象转换 (MapStruct)
├── domain/
│   ├── dto/
│   │   ├── FeatureDTO.java
│   │   └── SpotlightDTO.java
│   ├── entity/
│   │   ├── UserDO.java
│   │   ├── RoleDO.java
│   │   ├── AdminLogDO.java
│   │   ├── BizLogDO.java
│   │   ├── FeatureDO.java
│   │   └── SpotlightDO.java
│   ├── query/
│   │   └── OrderDateQuery.java       ← 订单日期查询条件
│   └── vo/
│       ├── FeatureVO.java
│       └── SpotlightVO.java
├── mapper/                           ← 对应 6 个实体的 Mapper
├── service/                          ← 对应 6 个实体的 Service 接口
├── service/impl/                     ← 对应 6 个实体的 Service 实现
└── plan/                             ← 规划中的 API
    ├── OrderManageApi.java
    ├── ProductCategoryManageApi.java
    └── ProductManageApi.java
```

## 管理职责

1. 管理 user 模块的用户数据
2. 管理 sugarcane 模块的后台价格信息
3. 焦点数据（Spotlight）与特性数据（Feature）管理

## 设计原则

- 管理接口以 `Admin` 结尾，需要管理员权限
- pitaya 的管理部分暂缓，相关接口已定义但后续可能删除
- 分散在其他模块中的管理接口后续将迁移到本模块

## 测试

`CodeGenerator.java` — MyBatis-Plus 代码生成器配置
