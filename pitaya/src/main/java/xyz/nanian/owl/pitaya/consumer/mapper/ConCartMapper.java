package xyz.nanian.owl.pitaya.consumer.mapper;


import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.pitaya.entity.ShoppingCartDO;

/**
 * consumer cart Mapper
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Mapper
public interface ConCartMapper {

    Integer insertCartDO(ShoppingCartDO shoppingCartDO);

    Integer updateCartDO(ShoppingCartDO shoppingCartDO);

    Integer deleteCartDO(Long userId,Long productId);

}
