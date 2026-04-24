package xyz.nanian.owl.admin.convert;


import org.mapstruct.Mapper;
import xyz.nanian.owl.admin.domain.entity.SpotlightDO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;

import java.util.List;

/**
 * Mapstruct
 *
 * @author slnt23
 * @since 2026/4/25
 */

@Mapper
public interface SpotlightConvert {
    SpotlightVO DOConvertVO(SpotlightDO spotlightDO);
    List<SpotlightVO> DOConvertVO(List<SpotlightDO> spotlightDOS);
}
