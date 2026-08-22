package xyz.nanian.owl.caishen.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.domain.vo.FundWatchVO;

import java.util.List;

/**
 * 关注记录与 VO 转换。
 *
 * <p>V1 关注列表由 {@code CaishenFundWatchMapper.selectWatchVOs} 的 XML 关联查询
 * 直接返回 {@link FundWatchVO}，本转换仅作内存拼装场景的补充。</p>
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Mapper(componentModel = "spring")
public interface CaishenFundWatchConvert {

    @Mapping(target = "fundName", ignore = true)
    @Mapping(target = "latestNav", ignore = true)
    @Mapping(target = "latestNavDate", ignore = true)
    @Mapping(target = "dailyReturnRate", ignore = true)
    @Mapping(target = "alertCount", ignore = true)
    FundWatchVO toVO(CaishenFundWatchDO watch);

    List<FundWatchVO> toVOList(List<CaishenFundWatchDO> watches);
}
