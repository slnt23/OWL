package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.mapper.ConCartMapper;
import xyz.nanian.owl.pitaya.consumer.service.ConCartService;
import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.entity.ShoppingCartDO;
import xyz.nanian.owl.pitaya.mapstruct.ShoppingCartConvert;
import xyz.nanian.owl.pitaya.vo.ShoppingCartVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.utils.jwt.UserContext;

import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.constant.RedisConstant.CART_KEY;
import static xyz.nanian.owl.constant.RedisConstant.CART_TIME_OUT;

/**
 * 消费者购物车Service实现
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Service
public class ConCartServiceImpl implements ConCartService {

    private final ConCartMapper conCartMapper;
    private final ShoppingCartConvert shoppingCartConvert;
    private final RedisTemplate<String,Object> redisTemplate;

    public ConCartServiceImpl(ConCartMapper conCartMapper,
                              ShoppingCartConvert shoppingCartConvert,
                              RedisTemplate<String,Object> redisTemplate) {
        this.shoppingCartConvert = shoppingCartConvert;
        this.conCartMapper = conCartMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 购物车新增商品
     * @param shoppingCartDTO
     * @return
     */
    @Override
    @BizLog(module = "购物车",action = "购物车新增商品")
    public Boolean saveProduct(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCartDO  shoppingCartDO = shoppingCartConvert.cartToDO(shoppingCartDTO);

        return conCartMapper.insertCartDO(shoppingCartDO) > 0;
    }

    /**
     * 购物车更新商品
     * @param shoppingCartDTO
     * @return
     */
    @Override
    @BizLog(module = "购物车",action = "购物车更新商品信息")
    public Boolean updateProduct(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCartDO shoppingCartDO = shoppingCartConvert.cartToDO(shoppingCartDTO);

        return conCartMapper.updateCartDO(shoppingCartDO)>0;
    }

    /**
     * 购物车删除商品
     * @param userId
     * @param productId
     * @return
     */
    @Override
    @BizLog(module = "购物车",action = "购物车删除商品")
    public Boolean deleteProduct(Long userId, Long productId) {

        Integer intDelete = conCartMapper.deleteCartDO(userId,productId);

        return intDelete>0;
    }

    /**
     * 查询购物车列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    @BizLog(module = "购物车",action = "查询购物车列表")
    public PageResult<ShoppingCartVO> listCart(Integer pageNum, Integer pageSize) {

//        这里 查询购物车的信息，但是商品表只有商品的id，没有，商品名，单价，封面图，这样也就是要按照id多次查询，这？
//        AI的建议真的善变，业务的标准还是实际就业才行，
//        这里的建议是 单表：DO；多表+ 聚合：用VO，DTO可以，

        Long userId = UserContext.getUserId();

        if(pageSize > 50){
            pageSize = 50;
        }

        String key = CART_KEY + userId;

//        先查Redis
//        PageResult<ShoppingCartVO> cache =
//                (PageResult<ShoppingCartVO>) redisTemplate.opsForValue().get(key);
        Object cache = redisTemplate.opsForValue().get(key);

        if(cache != null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            PageResult<ShoppingCartVO> result =
                    mapper.convertValue(cache, new TypeReference<PageResult<ShoppingCartVO>>() {});

            return result;
        }
//        redis没有，在数据库中，
        Page<ShoppingCartVO> pageParam = new Page<>(pageNum,pageSize);
        IPage<ShoppingCartVO> result = conCartMapper.pageCartVO(pageParam,userId);

        PageResult<ShoppingCartVO> pageResult = PageResult.create(result);

//        写入Redis

        redisTemplate.opsForValue().set(key,pageResult,CART_TIME_OUT, TimeUnit.MINUTES);

//        这里使用的是mybatis的wrapper 用法，前提：1.是要有数据库表对应的实体类DO/Entity，
//        失败，wrapper 是用再单表查询中的，
//        对于这种多表查询，就得用手写，
        return pageResult;
    }
}
