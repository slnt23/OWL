# Caishen 理财模块开发计划

| 属性     | 值            |
| -------- | ------------- |
| 状态     | 草稿          |
| 负责人   | Caishen Owner |
| 创建时间 | 2026-08-09    |
| 更新时间 | 2026-08-09    |

## 背景与目标

OWL 需要新增理财模块，代号 caishen，核心能力是查看基金和股票的变化并生成总结。
按照既定技术分工，本仓库只实现 Java 侧业务；Python 负责数据获取、清洗和 AI 分析，C++ 负责高性能计算，后续作为独立项目接入。
Java 侧需要提前定义外部服务边界，确保 Python/C++ 服务接入时不需要修改控制器和持久化层。

目标：

- 支持基金档案、基金净值、股票档案、股票日线、用户持仓与收益变化查询。
- 支持区间变化统计与总结生成。
- 预留 Python 数据源和 C++ 计算引擎的调用契约。
- 沿用 OWL 现有模块规范：`api` 依赖链、`Result<T>`、JWT、MyBatis-Plus、Knife4j。

非目标：

- 不在本仓库编写 Python/C++ 源码。
- 不做实盘交易、下单和风控。
- 不提供投资建议，总结仅作为辅助分析。

## 模块落点

- 模块目录：`watermelon/caishen`
- Maven artifactId：`caishen`
- 基础包：`xyz.nanian.owl.caishen`
- 接口前缀：`/caishen`
- API 分组：`理财中心-caishen`

注册动作：

1. 根 `pom.xml` 增加 `<module>watermelon/caishen</module>` 与 dependencyManagement。
2. `start/pom.xml` 增加 caishen 依赖。
3. `common/.../SpringdocConfig.java` 增加 `GroupedOpenApi`。
4. 默认所有接口需要登录；公开行情或健康检查如需免登录，再在 `SecurityConfig` 中配置白名单。

## 数据模型

### 表结构

`caishen_fund` 基金档案：

- id、fund_code、fund_name、fund_type、company_name、manager_name、risk_level、status
- `unique(fund_code)`

`caishen_fund_nav` 净值历史：

- id、fund_code、nav_date、unit_nav、accumulated_nav、daily_return_rate、source
- `unique(fund_code, nav_date)`

`caishen_stock` 股票档案：

- id、stock_code、stock_name、exchange、industry、status
- `unique(stock_code)`

`caishen_stock_daily` 股票日线：

- id、stock_code、trade_date、open_price、close_price、high_price、low_price、volume、amount、change_rate、source
- `unique(stock_code, trade_date)`

`caishen_holding` 用户持仓或关注：

- id、user_code、asset_type、asset_code、shares、cost_price、purchase_date、remark
- `unique(user_code, asset_type, asset_code)`

`caishen_summary` 总结任务与结果：

- id、user_code、asset_type、asset_code、period_type、start_date、end_date、metric_snapshot、summary_text、status、error_message

`caishen_sync_log` 数据同步日志：

- id、sync_type、provider、start_time、end_time、success_count、fail_count、status、error_message

### DDL

DDL 放在 `.docs/database/OWL/caishen/caishen_db.sql`，与现有 `sugarcane`、`pitaya` 数据库目录保持一致。

## 模块代码结构

```text
watermelon/caishen/
  src/main/java/xyz/nanian/owl/caishen/
    config/           # 模块私有配置，如异步线程池
    constant/         # 状态、周期、常量
    controller/       # FundController, StockController, HoldingController, SummaryController, SyncController
    domain/
      dto/            # 入参 DTO
      entity/         # DO
      vo/             # 出参 VO
    mapper/           # MyBatis-Plus Mapper
    mapstruct/        # MapStruct 转换
    service/
      impl/
    client/           # 外部服务客户端接口与实现
      python/         # 后续 Python 数据/AI 服务
      mock/           # 开发期 Mock
```

## 服务边界

### Java 侧职责

- 基金、股票档案、持仓、基金净值与股票日线查询与持久化。
- 用户维度隔离：持仓和总结按 `CurrentUserContext` 中的 `user_code` 过滤。
- 周期收益、区间涨跌幅、持仓成本与收益计算。
- 总结任务调度与状态管理。
- 作为 Python/C++ 服务的调用方，不直接实现行情源和 AI 大模型。

### 外部服务契约（预留）

`MarketDataClient` 负责基金净值和股票日线获取或导入：

- 现在：`MockMarketDataClient` 返回可配置的演示数据。
- 后续：`PythonMarketDataClient` 调用 Python 服务获取数据，或接收 Python 推送的数据。

`AssetAnalysisClient` 负责总结或复杂计算：

- 现在：`SpringAiAssetAnalysisClient` 基于现有 Spring AI 生成总结，保证闭环可跑。
- 后续：`PythonAssetAnalysisClient` 调用 Python AI 服务；C++ 指标服务通过同一接口扩展。
- 配置项：`caishen.analysis.provider=spring-ai|python|mock`。

### 对外 API（第一版）

- `GET /caishen/funds`：基金列表或搜索
- `GET /caishen/funds/{fundCode}/nav`：净值历史分页
- `POST /caishen/funds/{fundCode}/nav/import`：导入净值
- `GET /caishen/stocks`：股票列表或搜索
- `GET /caishen/stocks/{stockCode}/daily`：股票日线分页
- `POST /caishen/stocks/{stockCode}/daily/import`：导入股票日线
- `POST /caishen/holdings`：添加持仓
- `GET /caishen/holdings`：我的持仓
- `GET /caishen/holdings/{id}/changes?period=7D|30D|90D`：持仓区间变化
- `POST /caishen/summaries`：创建总结任务
- `GET /caishen/summaries/{id}`：查询总结结果
- `POST /caishen/sync`：手动触发数据同步

## 实施步骤

### 阶段 1：模块骨架

- 创建 `watermelon/caishen`、pom.xml 和基础包。
- 注册根 pom、start pom、Springdoc 分组。
- 验证 `mvn compile -pl watermelon/caishen -am`。

### 阶段 2：数据库与实体

- 编写 `caishen_db.sql`。
- 创建 DO、Mapper、MapStruct Convert。
- 验证表唯一键、索引和状态字段。

### 阶段 3：基础查询与持仓

- 基金档案、股票档案、基金净值、股票日线分页查询。
- 持仓增删改查。
- 接入 JWT 用户上下文。

### 阶段 4：变化统计

- 实现区间变化计算：最新净值、区间涨跌幅、持仓收益。
- 处理边界：无净值、除零、日期倒置、重复数据。
- 为计算逻辑编写单元测试。

### 阶段 5：总结任务

- 定义 `SummaryService` 和 `AssetAnalysisClient`。
- 先用 Spring AI 或 Mock 生成总结，保存到 `caishen_summary`。
- 异步执行，状态包含 PENDING、PROCESSING、SUCCESS、FAILED。
- 预留 Python 接入点。

### 阶段 6：定时同步

- 定义 `MarketDataClient` 与 `MarketDataSyncService`。
- 按日定时同步持仓或关注基金/股票数据，支持幂等 upsert。
- 记录 `caishen_sync_log`。

### 阶段 7：文档与验证

- 编写 ADR，记录 Java 宿主和 Python/C++ 外部服务边界。
- 更新 README 或文档中心索引。
- 执行 `mvn test -pl watermelon/caishen -am`。
- 启动 start 模块，验证 `/doc.html` 和基础接口。

## 待定问题

- 总结默认使用 Spring AI 还是 Mock，建议先用 Mock 或 Spring AI 保住闭环。
- 异步任务使用 RabbitMQ 还是线程池，建议第一版先使用线程池。
- 是否提供公开基金/股票行情免登录接口。
- 后续 Python 项目由 Java 主动拉取数据，还是 Python 主动推送到 Java。
- C++ 指标服务提供独立 HTTP 服务，还是由 Python 封装动态库。

## 变更历史

- 2026-08-09：创建开发计划草稿。

## 相关文档

- [ADR-005 Caishen 理财模块设计（基金/股票）](ADR-005-财神理财模块设计.md)
- [新模块添加手册](../guides/新模块开发指南.md)
- [文档中心](../README.md)
