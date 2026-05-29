# sugarcane 模块：价多多（价格管理）

## 定位

价格追踪管理系统，支持多来源、多地理位置的商品价格采集、比较与趋势分析。

## 包结构

```
xyz.nanian.owl.sugarcane
├── constant/
│   └── TimeGranularity.java         ← 时间粒度枚举（日/周/月/年）
├── controller/
│   ├── CategoryController.java       ← 商品分类管理
│   ├── GeolocationController.java   ← 地理位置管理
│   ├── ItemController.java          ← 商品项管理
│   ├── PriceItemMediaController.java ← 价格媒体管理
│   ├── RecordController.java        ← 价格记录管理
│   └── SourceController.java        ← 价格来源管理
├── domain/
│   ├── dto/
│   │   ├── BasePriceQueryDTO.java        ← 基础价格查询
│   │   ├── ItemDTO.java / ItemIntroDTO.java
│   │   ├── PriceCompareLocationDTO.java   ← 地区价格比较
│   │   ├── PriceCompareSourceDTO.java     ← 来源价格比较
│   │   ├── PriceLatestQueryDTO.java       ← 最新价格查询
│   │   ├── PricePageQueryDTO.java         ← 价格分页查询
│   │   ├── PriceRecordCreateDTO.java      ← 价格记录创建
│   │   └── PriceTrendQueryDTO.java        ← 趋势查询
│   ├── entity/
│   │   ├── CategoryDO.java          ← 分类实体
│   │   ├── GeoLocationDO.java       ← 地理位置实体
│   │   ├── ItemDO.java              ← 商品项实体
│   │   ├── PriceItemMediaDO.java    ← 价格媒体实体
│   │   ├── RecordDO.java            ← 价格记录实体
│   │   └── SourceDO.java            ← 数据来源实体
│   └── vo/
│       ├── CategoryTreeVO.java      ← 分类树
│       ├── ItemIntroVO.java / ItemVO.java
│       ├── LocationPriceVO.java     ← 地区价格视图
│       ├── PriceCompareVO.java      ← 价格比较视图
│       ├── PriceItemVO.java         ← 价格项视图
│       ├── PriceLatestVO.java       ← 最新价格视图
│       ├── PriceTrendPointVO.java   ← 价格趋势点
│       ├── PriceTrendVO.java        ← 价格趋势视图
│       └── SourceCompareVO.java     ← 来源比较视图
├── mapper/                           ← 对应 6 个实体的 Mapper
├── mapstruct/
│   └── ItemConvert.java              ← 商品对象转换
└── service/ + service/impl/         ← 对应 6 个实体的 Service
```

## 核心功能

- **多维度价格采集**：按地理位置、数据来源记录商品价格
- **价格比较**：跨地区、跨来源的价格对比
- **趋势分析**：支持日/周/月/年粒度的价格趋势
- **分类管理**：树形商品分类体系

## 数据模型关系

```
Category ──→ Item ──→ Record ──→ PriceItemMedia
                │
Source ─────────┤
GeoLocation ────┘
```
