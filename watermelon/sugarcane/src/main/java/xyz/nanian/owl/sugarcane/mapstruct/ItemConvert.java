package xyz.nanian.owl.sugarcane.mapstruct;


import org.mapstruct.Mapper;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemIntroVO;

import java.util.List;

/**
 * mapstruct
 *
 * @author slnt23
 * @since 2026/4/26
 */

@Mapper
public interface ItemConvert {
    List<ItemIntroVO> DOtoVO(List<ItemDO> itemDOS);
}
