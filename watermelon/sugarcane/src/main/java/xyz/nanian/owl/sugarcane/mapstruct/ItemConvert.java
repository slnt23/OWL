package xyz.nanian.owl.sugarcane.mapstruct;


import org.mapstruct.Mapper;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
//import xyz.nanian.owl.sugarcane.domain.vo.ItemIntroVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceItemVO;

import java.util.List;

/**
 * mapstruct
 *
 * @author slnt23
 * @since 2026/4/26
 */

@Mapper(componentModel = "spring")
public interface ItemConvert {
    List<PriceItemVO> DOtoVO(List<ItemDO> itemDOS);
}
