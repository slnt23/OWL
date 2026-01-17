package xyz.nanian.owl.pitaya.consumer.service;


import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;

/**
 * 消费者购物车Service
 *
 * @author slnt23
 * @since 2026/1/17
 */

public interface ConCartService {

    Boolean saveProduct(ShoppingCartDTO shoppingCartDTO);

    Boolean updateProduct(ShoppingCartDTO shoppingCartDTO);

    Boolean deleteProduct(Long userId,Long productId);
}
