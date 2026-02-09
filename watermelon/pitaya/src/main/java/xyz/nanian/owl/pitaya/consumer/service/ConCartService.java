package xyz.nanian.owl.pitaya.consumer.service;


import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.query.ShoppingCartQuery;
import xyz.nanian.owl.pitaya.vo.ShoppingCartVO;
import xyz.nanian.owl.result.PageResult;

/**
 * 消费者购物车Service
 *
 * @author slnt23
 * @since 2026/1/17
 */

public interface ConCartService {

    /**
     * 新增商品
     * @param shoppingCartDTO
     * @return
     */
    Boolean saveProduct(ShoppingCartDTO shoppingCartDTO);

    /**
     * 更新购物车中商品
     * @param shoppingCartDTO
     * @return
     */
    Boolean updateProduct(ShoppingCartDTO shoppingCartDTO);

    /**
     * 删除购物车中商品
     * @param userId
     * @param productId
     * @return
     */
    Boolean deleteProduct(Long userId,Long productId);

    /**
     * 查询购物车列表商品
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult<ShoppingCartVO> listCart(Integer pageNum, Integer pageSize);

}
