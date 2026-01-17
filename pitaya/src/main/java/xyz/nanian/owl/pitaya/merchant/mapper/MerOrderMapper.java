package xyz.nanian.owl.pitaya.merchant.mapper;


import org.apache.ibatis.annotations.Mapper;

/**
 * 商家订单Mapper
 *
 * @author slnt23
 * @since 2026/1/18
 */

@Mapper
public interface MerOrderMapper {

    /**
     * 更新订单状态，TODO 感觉这里以后可以优化
     * @param orderId
     * @param orderStatus
     * @return
     */
    Integer updateOrder(Long orderId, Integer orderStatus);

}
