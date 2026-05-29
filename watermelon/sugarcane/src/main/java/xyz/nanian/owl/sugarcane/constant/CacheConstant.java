package xyz.nanian.owl.sugarcane.constant;

/**
 * sugarcane 模块 Redis 缓存常量
 *
 * @author slnt23
 * @since 2026/5/29
 */
public final class CacheConstant {

    private CacheConstant() {}

    // ==================== 缓存名（对应 @Cacheable 的 cacheNames） ====================

    /** 最新价格，TTL 较短（有新数据录入即失效） */
    public static final String PRICE_LATEST = "priceLatest";

    /** 价格趋势，历史数据不可变，TTL 较长 */
    public static final String PRICE_TREND = "priceTrend";

    /** 地区价格对比 */
    public static final String PRICE_COMPARE_LOCATION = "priceCompareLocation";

    /** 来源价格对比 */
    public static final String PRICE_COMPARE_SOURCE = "priceCompareSource";

    /** 物品分页列表 */
    public static final String ITEM_PAGE = "itemPage";

    /** 分类树（几乎不变） */
    public static final String CATEGORY_TREE = "categoryTree";

    /** 来源列表 */
    public static final String SOURCE_LIST = "sourceList";

    /** 地理位置树 */
    public static final String GEO_TREE = "geoTree";

    /** 地理位置子节点 */
    public static final String GEO_CHILDREN = "geoChildren";

    // ==================== TTL 建议（在 RedisCacheManager 中配置） ====================

    /** 价格类数据：1 分钟 */
    public static final int TTL_PRICE = 1;

    /** 物品/列表类：5 分钟 */
    public static final int TTL_ITEM = 5;

    /** 参考数据（分类/来源/地理）：30 分钟 */
    public static final int TTL_REFERENCE = 30;

    // ==================== 条件常量 ====================

    /** 仅在结果不为 null 时缓存 */
    public static final String UNLESS_NULL = "#result == null";
}
