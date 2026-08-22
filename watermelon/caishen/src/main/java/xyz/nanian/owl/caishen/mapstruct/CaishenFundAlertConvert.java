package xyz.nanian.owl.caishen.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundAlertDO;
import xyz.nanian.owl.caishen.domain.vo.FundAlertVO;

import java.util.List;

/**
 * 提醒规则与 VO 转换。fundCode / fundName 由 service 关联查询后填充。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Mapper(componentModel = "spring")
public interface CaishenFundAlertConvert {

    @Mapping(target = "fundCode", ignore = true)
    @Mapping(target = "fundName", ignore = true)
    FundAlertVO toVO(CaishenFundAlertDO alert);

    List<FundAlertVO> toVOList(List<CaishenFundAlertDO> alerts);
}
