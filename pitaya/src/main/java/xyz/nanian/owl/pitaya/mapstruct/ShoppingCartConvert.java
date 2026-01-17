package xyz.nanian.owl.pitaya.mapstruct;


import org.mapstruct.Mapper;
import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.entity.ShoppingCartDO;

/**
 * 购物车映射Map
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Mapper(componentModel = "spring")
public interface ShoppingCartConvert {

    ShoppingCartDO cartToDO(ShoppingCartDTO shoppingCartDTO);
}
