package xyz.nanian.owl.pitaya.merchant.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.merchant.mapper.MerOrderMapper;
import xyz.nanian.owl.pitaya.merchant.service.MerOrderService;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.constant.RedisConstant.MERCHANT_ORDER_KEY;
import static xyz.nanian.owl.constant.RedisConstant.MERCHANT_ORDER_TIME_OUT;

/**
 * 商家订单Service Impl
 *
 * @author slnt23
 * @since 2026/1/18
 */

@Service
public class MerOrderServiceImpl implements MerOrderService {

    private final MerOrderMapper merOrderMapper;
    private final RedisTemplate redisTemplate;

    public MerOrderServiceImpl(MerOrderMapper merOrderMapper,
                               RedisTemplate redisTemplate) {
        this.merOrderMapper = merOrderMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 更新状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "更新订单状态")
    public Boolean updateOrderStatus(Long orderId, Integer orderStatus) {

        Integer intUpdate = merOrderMapper.updateOrder(orderId,orderStatus);
        return intUpdate>0;
    }

    /**
     * 查询指定用户订单列表，分页
     * 这里且是某个用户，某个商家的才行，
     * @param pageNum
     * @param pageSize
     * @param searchedUserId
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "查询指定用户订单列表")
    public PageResult<OrderListVO> listOrders(Integer pageNum,Integer pageSize,Long searchedUserId) {

//        用户在该商家的订单，
//        商家Id
        Long userId = UserContext.getUserId();
//        搜索用户Id
        String key= MERCHANT_ORDER_KEY + searchedUserId + userId;
        if(pageSize > 50){
            pageSize = 50;
        }

//        TODO 以下的这个方法只是暂时的，后续要找更好的方法替代
        PageResult<OrderListVO> cache =
                (PageResult<OrderListVO>) redisTemplate.opsForValue().get(key);
        if(cache!=null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            PageResult<OrderListVO> result =
                    mapper.convertValue(cache,new  TypeReference<PageResult<OrderListVO>>(){});
            return result;
        }

        Page<OrderListVO> page = new Page<>(pageNum,pageSize);
        IPage<OrderListVO> result = merOrderMapper.pageOrderList(page,searchedUserId);
        PageResult<OrderListVO> pageResult = PageResult.create(result);

        redisTemplate.opsForValue().set(key,pageResult,MERCHANT_ORDER_TIME_OUT, TimeUnit.MINUTES);

        return pageResult;
    }
}
