package xyz.nanian.owl.caishen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;

/**
 * 基金净值历史 Mapper。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Mapper
public interface CaishenFundNavMapper extends BaseMapper<CaishenFundNavDO> {

    /**
     * 按 (fund_code, nav_date) 幂等 upsert 净值记录。
     *
     * @param nav 净值记录
     * @return 影响行数
     */
    int upsertNav(CaishenFundNavDO nav);
}
