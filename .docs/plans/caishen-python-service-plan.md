# Caishen Python 服务开发计划

| 属性     | 值                 |
| -------- | ------------------ |
| 状态     | 草稿               |
| 负责人   | Caishen Owner      |
| 创建时间 | 2026-08-22         |
| 更新时间 | 2026-08-22         |
| 仓库     | 独立仓库，不在 OWL |

## 背景与目标

按照 [ADR-005](../adr/ADR-005-财神理财模块设计.md) 的技术分工，OWL Java 仓库作为宿主，Python 负责数据获取/清洗和 AI 分析，C++ 负责高性能计算。Java 侧通过 `MarketDataClient` 和 `AssetAnalysisClient` 两个接口隔离外部能力，Python 服务只需实现既有 REST 契约即可接入，不修改 Java 控制器和持久化层。

本计划定义 Python 服务的 V1 范围、项目结构、REST 契约、实施步骤和与 Java 侧的对接方式。

### V1 目标

- 提供基金净值数据获取服务，替代 Java 侧的 `MockMarketDataClient`。
- 提供基金区间变化 AI 总结服务，替代 Java 侧的 `SpringAiAssetAnalysisClient`。
- 以独立 HTTP 服务运行，Java 侧通过 REST 调用。

### 非目标

- 不做股票行情获取（V2 扩展）。
- 不做实盘交易、下单和风控。
- 不提供投资建议，AI 总结仅作为辅助分析。
- 不在 OWL Java 仓库中编写 Python 源码。

## 技术选型

| 维度       | 选择                        | 原因                                          |
| ---------- | --------------------------- | --------------------------------------------- |
| 语言       | Python 3.11+                | 数据生态成熟，AI/ML 库丰富                    |
| Web 框架   | FastAPI                     | 异步原生、自动生成 OpenAPI 文档、类型安全     |
| 行情数据源 | akshare / 天天基金开放接口  | akshare 封装了国内基金/股票行情 API，免费可用 |
| AI 框架    | LangChain + OpenAI 兼容协议 | 与 OWL crow 模块一致，复用 DeepSeek 配置      |
| 数据验证   | Pydantic V2                 | FastAPI 内置，与 Java 侧 DTO 对齐             |
| 包管理     | uv / poetry                 | uv 速度快，poetry 生态成熟，团队自选          |
| 容器化     | Docker                      | 统一部署，与 OWL Java 服务编排                |

## 项目结构

```text
caishen-python/
  pyproject.toml                  # 项目元数据与依赖
  Dockerfile
  .env.example                    # 环境变量模板
  src/
    caishen/
      __init__.py
      main.py                     # FastAPI 应用入口，uvicorn 启动
      config.py                   # Settings（Pydantic BaseSettings）
      api/
        __init__.py
        router.py                 # 路由汇总
        market_data.py            # /market/fund/nav 端点
        analysis.py               # /analysis/summary 端点
        health.py                 # /health 健康检查
      domain/
        __init__.py
        models.py                 # Pydantic 请求/响应模型
      service/
        __init__.py
        market_service.py         # 行情数据获取与清洗
        analysis_service.py       # AI 总结生成
      client/
        __init__.py
        akshare_client.py         # akshare 行情源客户端
        llm_client.py             # LLM 调用客户端（OpenAI 兼容）
      util/
        __init__.py
        date_util.py              # 日期工具
        rate_limiter.py           # 请求频率限制
  tests/
    test_market_service.py
    test_analysis_service.py
    test_api/
      test_market_data.py
      test_analysis.py
```

## REST 契约

Python 服务以 HTTP 服务运行，Java 侧通过 `RestTemplate` 或 `WebClient` 调用。以下契约为 V1 必须实现的最小接口集。

### 通用约定

- 基础路径：`/api/caishen-python`
- 响应格式：统一 JSON `{ "code": 200, "message": "success", "data": ... }`，与 OWL `Result<T>` 对齐
- 错误码：4xx 客户端错误，5xx 服务端错误，`code` 字段携带业务错误码
- 超时：Java 侧调用超时 10s（行情）、30s（AI 总结）

### 健康检查

```
GET /api/caishen-python/health
→ { "code": 200, "data": { "status": "UP", "version": "1.0.0" } }
```

### 基金净值获取

```
POST /api/caishen-python/market/fund/nav
Content-Type: application/json

请求体：
{
  "fundCodes": ["000001", "005827"],
  "navDate": "2026-08-22"          // 可选，默认当天
}

响应体：
{
  "code": 200,
  "data": [
    {
      "fundCode": "000001",
      "fundName": "华夏成长混合",
      "navDate": "2026-08-22",
      "unitNav": 1.5234,
      "accumulatedNav": 3.8912,
      "dailyReturnRate": 1.2345
    },
    {
      "fundCode": "005827",
      "fundName": "易方达蓝筹精选混合",
      "navDate": "2026-08-22",
      "unitNav": 2.1567,
      "accumulatedNav": 2.1567,
      "dailyReturnRate": -0.5621
    }
  ]
}
```

**Java 侧对接**：`PythonMarketDataClient` 实现如下逻辑——

```java
public class PythonMarketDataClient implements MarketDataClient {
    private final RestTemplate restTemplate;
    private final String pythonBaseUrl; // 从 caishen.market.python-url 配置

    @Override
    public List<FundNavData> fetchLatestNav(List<String> fundCodes) {
        var request = Map.of("fundCodes", fundCodes);
        var response = restTemplate.postForObject(
            pythonBaseUrl + "/api/caishen-python/market/fund/nav",
            request, FundNavResponse.class);
        return response.getData();
    }
}
```

### 基金区间净值批量获取

```
POST /api/caishen-python/market/fund/nav/history
Content-Type: application/json

请求体：
{
  "fundCodes": ["000001"],
  "startDate": "2026-08-16",
  "endDate": "2026-08-22"
}

响应体：
{
  "code": 200,
  "data": [
    {
      "fundCode": "000001",
      "navList": [
        { "navDate": "2026-08-16", "unitNav": 1.4890, "accumulatedNav": 3.8568, "dailyReturnRate": 0.3211 },
        { "navDate": "2026-08-17", "unitNav": 1.5012, "accumulatedNav": 3.8690, "dailyReturnRate": 0.8197 },
        ...
      ]
    }
  ]
}
```

### AI 总结生成

```
POST /api/caishen-python/analysis/summary
Content-Type: application/json

请求体：
{
  "metricSnapshot": "{\"funds\":[{\"code\":\"000001\",\"name\":\"华夏成长\",\"weekChange\":2.34,\"dayChange\":1.23},...]}",
  "periodType": "WEEKLY",
  "language": "zh-CN"              // 可选，默认中文
}

响应体：
{
  "code": 200,
  "data": {
    "summaryText": "本周市场整体震荡上行，您关注的股票型基金表现优于混合型...",
    "tokenUsage": { "promptTokens": 850, "completionTokens": 320, "totalTokens": 1170 }
  }
}
```

**Java 侧对接**：`PythonAssetAnalysisClient` 实现如下逻辑——

```java
public class PythonAssetAnalysisClient implements AssetAnalysisClient {
    private final RestTemplate restTemplate;
    private final String pythonBaseUrl;

    @Override
    public String generateSummary(String metricSnapshot, String periodType) {
        var request = Map.of("metricSnapshot", metricSnapshot, "periodType", periodType);
        var response = restTemplate.postForObject(
            pythonBaseUrl + "/api/caishen-python/analysis/summary",
            request, SummaryResponse.class);
        return response.getData().getSummaryText();
    }
}
```

## 服务详细设计

### MarketService — 行情数据获取

```python
class MarketService:
    async def fetch_latest_nav(self, fund_codes: list[str], nav_date: date | None = None) -> list[FundNavData]:
        """获取指定基金代码的最新净值。

        1. 调用 akshare 获取基金净值数据
        2. 清洗：处理缺失值、异常值、日期格式统一
        3. 计算日收益率 = (当日净值 - 前日净值) / 前日净值 * 100
        4. 返回标准化数据
        """
        ...

    async def fetch_nav_history(self, fund_codes: list[str], start_date: date, end_date: date) -> list[FundNavHistory]:
        """获取指定基金在日期区间内的净值历史。"""
        ...
```

**akshare 使用要点**：

- `ak.fund_etf_hist_sina()`：获取 ETF 基金历史净值
- `ak.fund_open_fund_info()`：获取开放式基金信息与净值
- 请求频率限制：akshare 对同一 IP 有频率限制，需在 `RateLimiter` 中配置间隔（建议 0.5s/次）
- 数据缓存：对同一天同一基金的净值结果做内存缓存，避免重复请求

### AnalysisService — AI 总结生成

```python
class AnalysisService:
    async def generate_summary(self, metric_snapshot: str, period_type: str, language: str = "zh-CN") -> SummaryResult:
        """根据指标快照生成总结文本。

        1. 解析 metric_snapshot JSON
        2. 构建 system prompt（角色：理财分析师，语言：中文，免责声明）
        3. 构建 user prompt（指标数据 + 分析要求）
        4. 调用 LLM 生成总结
        5. 返回总结文本和 token 用量
        """
        ...
```

**LLM 配置**：

- 模型：DeepSeek（与 OWL crow 模块一致，OpenAI 兼容协议）
- API Key：从环境变量 `DEEPSEEK_API_KEY` 读取
- Base URL：从环境变量 `LLM_BASE_URL` 读取，默认 `https://api.deepseek.com`
- System Prompt 模板：

```
你是一位专业的基金理财分析师。请根据提供的基金区间变化数据，生成简洁、客观的总结。
要求：
1. 总结各基金涨跌情况和可能原因
2. 对比不同基金的表现差异
3. 提供下一周期关注要点
4. 不提供具体投资建议
5. 在末尾添加免责声明：以上分析仅供参考，不构成投资建议
```

## 配置项

```python
class CaishenPythonSettings(BaseSettings):
    # 服务
    app_name: str = "caishen-python"
    app_version: str = "1.0.0"
    host: str = "0.0.0.0"
    port: int = 8100

    # 行情数据源
    market_source: str = "akshare"          # akshare | mock
    akshare_rate_limit_seconds: float = 0.5 # akshare 请求间隔
    nav_cache_ttl_seconds: int = 3600       # 净值缓存 TTL

    # AI
    llm_base_url: str = "https://api.deepseek.com"
    llm_api_key: str                         # 从环境变量读取
    llm_model: str = "deepseek-chat"
    llm_max_tokens: int = 2000
    llm_temperature: float = 0.7

    class Config:
        env_prefix = "CAISHEN_"
```

## Java 侧切换配置

当 Python 服务就绪后，Java 侧只需修改配置即可切换：

```yaml
caishen:
  market:
    provider: python # mock → python
    python-url: http://caishen-python:8100
  analysis:
    provider: python # spring-ai → python
    python-url: http://caishen-python:8100
```

Java 侧新增 `PythonMarketDataClient` 和 `PythonAssetAnalysisClient` 两个实现类，通过 `@ConditionalOnProperty` 按配置激活：

```java
@Bean
@ConditionalOnProperty(name = "caishen.market.provider", havingValue = "python")
public MarketDataClient pythonMarketDataClient() { ... }

@Bean
@ConditionalOnProperty(name = "caishen.market.provider", havingValue = "mock", matchIfMissing = true)
public MarketDataClient mockMarketDataClient() { ... }
```

## 实施步骤

### 阶段 1：项目骨架

- 初始化 `caishen-python` 项目，配置 `pyproject.toml`、FastAPI、uvicorn。
- 实现 `main.py`、`config.py`、健康检查端点。
- 编写 `Dockerfile`。
- 验证 `uvicorn src.caishen.main:app --reload` 可启动。

### 阶段 2：行情数据服务

- 实现 `AkshareClient`：封装 akshare 基金净值获取接口。
- 实现 `RateLimiter`：控制请求频率。
- 实现 `MarketService`：数据获取 + 清洗 + 日收益率计算。
- 实现 `/market/fund/nav` 和 `/market/fund/nav/history` 端点。
- 编写单元测试和集成测试。

### 阶段 3：AI 总结服务

- 实现 `LlmClient`：OpenAI 兼容协议调用 DeepSeek。
- 实现 `AnalysisService`：prompt 构建 + LLM 调用 + 结果解析。
- 实现 `/analysis/summary` 端点。
- 编写单元测试（mock LLM 响应）。

### 阶段 4：Mock 模式

- 实现 `MockMarketService`：返回可配置的演示数据，用于开发期和 CI 测试。
- 通过 `CAISHEN_MARKET_SOURCE=mock` 切换。

### 阶段 5：容器化与部署

- 完善 `Dockerfile`（多阶段构建，slim 镜像）。
- 编写 `docker-compose.yml`（与 OWL Java 服务编排）。
- 配置健康检查和优雅关闭。

### 阶段 6：Java 侧对接

- 在 Java caishen 模块中实现 `PythonMarketDataClient` 和 `PythonAssetAnalysisClient`。
- 添加 `@ConditionalOnProperty` 切换逻辑。
- 修改 `CaishenConfig` 增加 `python-url` 配置项。
- 集成测试：Java 侧调用 Python 服务，验证完整链路。

### 阶段 7：文档与验证

- 编写 Python 服务 README（启动方式、配置项、REST 契约）。
- 更新 ADR-005 和 V1 方案，标记 Python 服务已就绪。
- 端到端测试：Java 定时调度 → Python 行情 → 阈值检查 → 邮件提醒。

## 与 C++ 服务的关系

V1 中 C++ 指标计算服务暂不实现，Python 服务直接承担计算职责（日收益率、区间涨跌幅等）。当计算量增长到需要 C++ 加速时（如蒙特卡洛模拟、大规模回测），C++ 服务通过以下方式接入：

1. C++ 提供独立 HTTP 服务（gRPC 或 REST），Python 作为代理调用。
2. 或 C++ 编译为共享库，Python 通过 `ctypes` / `pybind11` 直接调用。
3. Java 侧无需改动，仍然调用 Python 的 `/analysis/summary` 端点。

## 待定问题

- akshare 的频率限制和可用性需要实际测试，可能需要备用数据源。
- LLM 调用的 token 消耗和成本需要监控，是否需要设置单次上限。
- Python 服务的部署方式：独立容器还是与 Java 共存。
- 是否需要 Python 侧也做用户认证，还是完全信任 Java 侧的内网调用。
- 数据缓存策略：内存缓存还是 Redis，TTL 如何设置。

## 变更历史

- 2026-08-22：创建 Python 服务开发计划草稿。

## 相关文档

- [ADR-005 Caishen 理财模块设计](../adr/ADR-005-财神理财模块设计.md)
- [Caishen V1 基金监控与提醒方案](caishen-v1-fund-monitor-plan.md)
- [Caishen C++ 服务开发计划](caishen-cpp-service-plan.md)
- [文档中心](../README.md)
