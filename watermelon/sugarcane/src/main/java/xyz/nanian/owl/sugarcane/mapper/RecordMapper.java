package xyz.nanian.owl.sugarcane.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareLocationDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceCompareSourceDTO;
import xyz.nanian.owl.sugarcane.domain.dto.PriceTrendQueryDTO;
import xyz.nanian.owl.sugarcane.domain.entity.RecordDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.sugarcane.domain.vo.*;

import java.util.List;

/**
 * <p>
 * 价格记录表（时间序列数据） Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Mapper
public interface RecordMapper extends BaseMapper<RecordDO> {

//    查询最新的
    PriceLatestVO selectLatest(@Param("item_id") Long id);

//    查询趋势
    List<PriceTrendVO> selectTrend(@Param("query") PriceTrendQueryDTO query);

    // 查询单个物品信息
    @Select("SELECT id AS itemId, item_name AS itemName, unit, specification, " +
            "(SELECT category_name FROM price_category WHERE id = category_id) AS categoryName " +
            "FROM price_item " +
            "WHERE id = #{itemId} OR item_code = #{itemCode} LIMIT 1")
    PriceItemVO selectItemByIdOrCode(@Param("itemId") Long itemId,
                                     @Param("itemCode") String itemCode);

    // 查询指定时间的各地区价格
    List<LocationPriceVO> selectLocationPrices(@Param("dto") PriceCompareLocationDTO dto);

    // 查询指定时间各来源价格
    List<SourceCompareVO> selectSourcePrices(@Param("dto") PriceCompareSourceDTO dto);
}
