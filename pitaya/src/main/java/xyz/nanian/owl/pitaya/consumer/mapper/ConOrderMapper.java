package xyz.nanian.owl.pitaya.consumer.mapper;


import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.pitaya.entity.OrderDO;
import xyz.nanian.owl.pitaya.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.query.AddressQuery;
import xyz.nanian.owl.pitaya.query.OrderQuery;

import java.util.List;

/**
 * 消费者订单Mapper
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Mapper
public interface ConOrderMapper {

    /**
     * 新增订单
     * @param orderDO
     * @return
     */
    Integer insertOrder(OrderDO orderDO);

    /**
     * 新增订单明细
     * @param orderDetailDO
     * @return
     */
    Integer insertOrderDetail(OrderDetailDO orderDetailDO);

    /**
     * 新增订单明细List
     * @param orderDetailDO
     * @return
     */
    Integer insertOrderDetailList(List<OrderDetailDO> orderDetailDO);

    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    Integer updateOrder(Long orderId,Integer orderStatus);

    /**
     * 查询订单明细
     * @param orderId
     * @return
     */
    List<OrderDetailDO> selectOrderDetail(Long orderId);

    /**
     * 查询订单
     * @param orderQuery
     * @return
     */
    OrderDO selectOrder(OrderQuery orderQuery);

    /**
     * 查询地址
     * @param addressQuery
     * @return
     */
    UserAddressDO selectAddress(AddressQuery addressQuery);

}
