package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.mapper.ConCartMapper;
import xyz.nanian.owl.pitaya.consumer.service.ConCartService;
import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.entity.ShoppingCartDO;
import xyz.nanian.owl.pitaya.mapstruct.ShoppingCartConvert;
import xyz.nanian.owl.pitaya.query.ShoppingCartQuery;
import xyz.nanian.owl.pitaya.vo.ShoppingCartVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 消费者购物车Service实现
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Service
public class ConCartServiceImpl implements ConCartService {

    private ConCartMapper conCartMapper;
    private ShoppingCartConvert shoppingCartConvert;

    public ConCartServiceImpl(ConCartMapper conCartMapper, ShoppingCartConvert shoppingCartConvert) {
        this.shoppingCartConvert = shoppingCartConvert;
        this.conCartMapper = conCartMapper;
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
    public PageResult<ShoppingCartVO> listCart(Integer pageNum, Integer pageSize) {

//        这里 查询购物车的信息，但是商品表只有商品的id，没有，商品名，单价，封面图，这样也就是要按照id多次查询，这？
//        AI的建议真的善变，业务的标准还是实际就业才行，
//        这里的建议是 单表：DO；多表+ 聚合：用VO，DTO可以，

//        这里后期得换成从后端获取用户Id,怎么能前端传递呢？目前自定义，
//        Long userId = shoppingCartQuery.getUserId();

        Long userId = 4L;

        if(pageSize > 50){
            pageSize = 50;
        }

        Page<ShoppingCartVO> pageParam = new Page<>(pageNum,pageSize);

//        这里使用的是mybatis的wrapper 用法，前提：1.是要有数据库表对应的实体类DO/Entity，
//        失败，wrapper 是用再单表查询中的，

//        对于这种多表查询，就得用手写，
        IPage<ShoppingCartVO> result = conCartMapper.pageCartVO(pageParam,userId);

        return PageResult.create(result);
    }
}
