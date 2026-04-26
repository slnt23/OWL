package xyz.nanian.owl.pitaya.consumer.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.domain.entity.OrderDO;
import xyz.nanian.owl.pitaya.domain.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.domain.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.domain.query.AddressQuery;
import xyz.nanian.owl.pitaya.domain.query.OrderDTO;
import xyz.nanian.owl.pitaya.domain.vo.OrderListVO;

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
    OrderDO selectOrder(OrderDTO orderQuery);

    /**
     * 查询地址
     * @param addressQuery
     * @return
     */
    UserAddressDO selectAddress(AddressQuery addressQuery);

    /**
     * 查询订单列表
     * @param page
     * @param userId
     * @return
     */
    IPage<OrderListVO> pageOrderList(Page<?> page,
                                     @Param("userId") Long userId);

}
