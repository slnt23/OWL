package xyz.nanian.owl.pitaya.consumer.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.domain.entity.ShoppingCartDO;
import xyz.nanian.owl.pitaya.domain.vo.ShoppingCartVO;

/**
 * consumer cart Mapper
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Mapper
public interface ConCartMapper {

    /**
     * 购物车新增商品
     * @param shoppingCartDO
     * @return
     */
    Integer insertCartDO(ShoppingCartDO shoppingCartDO);

    /**
     * 更新购物车
     * @param shoppingCartDO
     * @return
     */
    Integer updateCartDO(ShoppingCartDO shoppingCartDO);

    /**
     * 删除购物车中物品
     * @param userId
     * @param productId
     * @return
     */
    Integer deleteCartDO(Long userId,Long productId);

    /**
     * 查询购物车列表
     * @param page
     * @param userId
     * @return
     */
    IPage<ShoppingCartVO> pageCartVO(Page<?> page,
                                     @Param("userId") Long userId);

}
