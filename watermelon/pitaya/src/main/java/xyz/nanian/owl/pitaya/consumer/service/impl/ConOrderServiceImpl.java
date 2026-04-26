package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.mapper.ConOrderMapper;
import xyz.nanian.owl.pitaya.consumer.service.ConOrderService;
import xyz.nanian.owl.pitaya.domain.entity.OrderDO;
import xyz.nanian.owl.pitaya.domain.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.domain.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.mapstruct.OrderConvert;
import xyz.nanian.owl.pitaya.domain.query.AddressQuery;
import xyz.nanian.owl.pitaya.domain.query.OrderDTO;
import xyz.nanian.owl.pitaya.domain.vo.AddressVO;
import xyz.nanian.owl.pitaya.domain.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.domain.vo.OrderItemVO;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.ResultPage;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.infrastructure.rabbitmq.RabbitMQConstant.ORDER_QUEUE;
import static xyz.nanian.owl.infrastructure.rabbitmq.RabbitMQConstant.ORDER_ROUTING_KEY;
import static xyz.nanian.owl.pitaya.constant.ShopConstant.ORDER_KEY;
import static xyz.nanian.owl.pitaya.constant.ShopConstant.ORDER_TIME_OUT;

/**
 * 消费者订单ServiceImpl
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Service
public class ConOrderServiceImpl implements ConOrderService {

    private final ConOrderMapper conOrderMapper;
    private final OrderConvert orderConvert;
    private final RedisTemplate<String ,Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    public ConOrderServiceImpl(ConOrderMapper conOrderMapper,
                               OrderConvert orderConvert,
                               RedisTemplate redisTemplate,
                               RabbitTemplate rabbitTemplate) {
        this.conOrderMapper = conOrderMapper;
        this.orderConvert = orderConvert;
        this.redisTemplate = redisTemplate;
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 新增订单
     * @param orderDTO
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "新增订单")
    public Boolean saveOrder(xyz.nanian.owl.pitaya.dto.OrderDTO orderDTO) {

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

        Long userId = UserContext.getUserId();
        String orderCode = UUID.randomUUID().toString();
        orderDO.setOrderNo(orderCode);
        orderDO.setUserId(userId);
        OrderDTO orderQuery = new OrderDTO();
        orderQuery.setOrderCode(orderCode);

        Integer int1 = conOrderMapper.insertOrder(orderDO);

//        插入后再通过Code 查询订单的id,
        OrderDO order = conOrderMapper.selectOrder(orderQuery);
        Long orderId = order.getId();

        for(OrderDetailDO detail : orderDetailDO) {
            detail.setOrderId(orderId);
        }

        Integer int2 = conOrderMapper.insertOrderDetailList(orderDetailDO);

//        发送订单创建消息，
        sendOrderCreate(orderId);

        return int1>0 || int2>0;
    }


    public void sendOrderCreate(Long orderId) {
        rabbitTemplate.convertAndSend(
                ORDER_QUEUE,
                ORDER_ROUTING_KEY,
                orderId);
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
        OrderDTO orderQuery = new OrderDTO();
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
    public ResultPage<OrderListVO> listOrders(Integer pageNum, Integer pageSize) {

        Long userId = UserContext.getUserId();
        if(pageSize> 50){
            pageSize = 50;
        }

        String key = ORDER_KEY + userId;
//        先查Redis，
        ResultPage<OrderListVO> cache =
                (ResultPage<OrderListVO>) redisTemplate.opsForValue().get(key);

        if(cache!=null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ResultPage<OrderListVO> result =
                    mapper.convertValue(cache,new TypeReference<ResultPage<OrderListVO>>() {});

            return result;
        }

//        Redis没有，查Mysql，
        Page<OrderListVO> page = new  Page<>(pageNum,pageSize);
        IPage<OrderListVO> result = conOrderMapper.pageOrderList(page,userId);

        ResultPage<OrderListVO> resultPage = ResultPage.create(result);

//        写入Redis
        redisTemplate.opsForValue().set(key, resultPage,ORDER_TIME_OUT, TimeUnit.MINUTES);

        return resultPage;
    }


}
