package xyz.nanian.owl.pitaya.consumer.service.impl;


import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.mapper.ConCartMapper;
import xyz.nanian.owl.pitaya.consumer.service.ConCartService;
import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.entity.ShoppingCartDO;
import xyz.nanian.owl.pitaya.mapstruct.ShoppingCartConvert;

/**
 * 消费者购物车Service实现
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Service
public class ConCartServiceImpl implements ConCartService {

    private ConCartMapper conCartMapper;
    private ShoppingCartConvert shoppingCartConvert;

    public ConCartServiceImpl(ConCartMapper conCartMapper, ShoppingCartConvert shoppingCartConvert) {
        this.shoppingCartConvert = shoppingCartConvert;
        this.conCartMapper = conCartMapper;
    }

    @Override
    @BizLog(module = "购物车",action = "购物车新增商品")
    public Boolean saveProduct(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCartDO  shoppingCartDO = shoppingCartConvert.cartToDO(shoppingCartDTO);

        return conCartMapper.insertCartDO(shoppingCartDO) > 0;
    }

    @Override
    @BizLog(module = "购物车",action = "购物车更新商品信息")
    public Boolean updateProduct(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCartDO shoppingCartDO = shoppingCartConvert.cartToDO(shoppingCartDTO);

        return conCartMapper.updateCartDO(shoppingCartDO)>0;
    }

    @Override
    @BizLog(module = "购物车",action = "购物车删除商品")
    public Boolean deleteProduct(Long userId, Long productId) {

        Integer intDelete = conCartMapper.deleteCartDO(userId,productId);

        return intDelete>0;
    }
}
