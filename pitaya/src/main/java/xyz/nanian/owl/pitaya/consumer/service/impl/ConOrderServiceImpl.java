package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.mapper.ConOrderMapper;
import xyz.nanian.owl.pitaya.consumer.service.ConOrderService;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.entity.OrderDO;
import xyz.nanian.owl.pitaya.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.mapstruct.OrderConvert;
import xyz.nanian.owl.pitaya.query.AddressQuery;
import xyz.nanian.owl.pitaya.query.OrderQuery;
import xyz.nanian.owl.pitaya.vo.AddressVO;
import xyz.nanian.owl.pitaya.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.vo.OrderItemVO;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;

import java.util.List;
import java.util.UUID;

/**
 * 消费者订单ServiceImpl
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Service
public class ConOrderServiceImpl implements ConOrderService {

    private ConOrderMapper conOrderMapper;
    private OrderConvert orderConvert;

    public ConOrderServiceImpl(ConOrderMapper conOrderMapper, OrderConvert orderConvert) {
        this.conOrderMapper = conOrderMapper;
        this.orderConvert = orderConvert;
    }

    /**
     * 新增订单
     * @param orderDTO
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "新增订单")
    public Boolean saveOrder(OrderDTO orderDTO) {

//        对于不同的来源是怎么处理？
        Long arId = orderDTO.getAddressId();
        AddressQuery addressQuery = new AddressQuery();
        addressQuery.setAddressId(arId);
        UserAddressDO userAddressDO = conOrderMapper.selectAddress(addressQuery);

        OrderDO orderDO = new OrderDO();
        List<OrderDetailDO> orderDetailDO = orderConvert.
                orderItemToOrderDetailDOList(orderDTO.getItems());

//        这里的数据类型没有考虑号，结果这里快照用地址id替代，
        orderDO.setAddressSnapshot(userAddressDO.getId()+"");


        String orderCode = UUID.randomUUID().toString();
        orderDO.setOrderNo(orderCode);
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setOrderCode(orderCode);

        Integer int1 = conOrderMapper.insertOrder(orderDO);

//        插入后再通过Code 查询订单的id,
        OrderDO order = conOrderMapper.selectOrder(orderQuery);
        Long orderId = order.getId();

//        TODO 通过token等获取userId，然后添加，

        for(OrderDetailDO detail : orderDetailDO) {
            detail.setOrderId(orderId);
        }

        Integer int2 = conOrderMapper.insertOrderDetailList(orderDetailDO);

        return int1>0 || int2>0;
    }

    /**
     * 更新订单
     * @param orderId
     * @param orderStatus
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "更新订单")
    public Boolean updateOrder(Long orderId, Integer orderStatus) {

        Integer intUpdate = conOrderMapper.updateOrder(orderId,orderStatus);
        return intUpdate>0;
    }

    /**
     * 查询订单详情
     * @param orderId
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "查询订单详情")
    public OrderDetailVO getOrderDetail(Long orderId) {

//        order detail
        List<OrderDetailDO> orderDetailDO = conOrderMapper.selectOrderDetail(orderId);
        List<OrderItemVO> orderItemVOS= orderConvert.orderDetailDOToItemVOList(orderDetailDO);

//        order center
        OrderQuery orderQuery = new OrderQuery();
        orderQuery.setId(orderId);
        OrderDO orderDO = conOrderMapper.selectOrder(orderQuery);

//        address
        Long userId = orderDO.getUserId();
        AddressQuery  addressQuery = new AddressQuery();
        addressQuery.setUserId(userId);
        UserAddressDO addressDO = conOrderMapper.selectAddress(addressQuery);
        AddressVO addressVO = orderConvert.addressDOToAddressVO(addressDO);

//        注入
        OrderDetailVO orderDetailVO = orderConvert.OrderDOToOrderDetailVO(orderDO);
        orderDetailVO.setAddress(addressVO);
        orderDetailVO.setItems(orderItemVOS);

        return orderDetailVO;
    }

    /**
     * 查询订单列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "用户订单列表")
    public PageResult<OrderListVO> listOrders(Integer pageNum,Integer pageSize) {

//        这里后期改为从后端获取用户的ID,目前自定义
//        Long userId = orderQuery.getUserId();
        Long userId = 1L;

        if(pageSize> 50){
            pageSize = 50;
        }

        Page<OrderListVO> page = new  Page<>(pageNum,pageSize);

        IPage<OrderListVO> result = conOrderMapper.pageOrderList(page,userId);

        return PageResult.create(result);
    }


}
