package xyz.nanian.owl.caishen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.domain.vo.FundWatchVO;

import java.util.List;

/**
 * 用户关注基金 Mapper。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Mapper
public interface CaishenFundWatchMapper extends BaseMapper<CaishenFundWatchDO> {

    /**
     * 查询某用户的关注列表（含基金名称、最新净值、提醒规则数）。
     *
     * @param userId 用户 ID
     * @return 关注出参列表
     */
    List<FundWatchVO> selectWatchVOs(@Param("userId") Long userId);
}
