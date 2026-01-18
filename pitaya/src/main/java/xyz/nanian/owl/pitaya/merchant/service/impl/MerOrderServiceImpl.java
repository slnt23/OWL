package xyz.nanian.owl.pitaya.merchant.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.merchant.mapper.MerOrderMapper;
import xyz.nanian.owl.pitaya.merchant.service.MerOrderService;
import xyz.nanian.owl.pitaya.vo.OrderListVO;
import xyz.nanian.owl.result.PageResult;

/**
 * 商家订单Service Impl
 *
 * @author slnt23
 * @since 2026/1/18
 */

@Service
public class MerOrderServiceImpl implements MerOrderService {

    private MerOrderMapper merOrderMapper;

    public MerOrderServiceImpl(MerOrderMapper merOrderMapper) {
        this.merOrderMapper = merOrderMapper;
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
     * 查询指定用户订单列表
     * @param pageNum
     * @param pageSize
     * @param searchedUserId
     * @return
     */
    @Override
    @BizLog(module = "订单",action = "查询指定用户订单列表")
    public PageResult<OrderListVO> listOrders(Integer pageNum,Integer pageSize,Long searchedUserId) {

        if(pageSize > 50){
            pageSize = 50;
        }

        Page<OrderListVO> page = new Page<>(pageNum,pageSize);

        IPage<OrderListVO> result = merOrderMapper.pageOrderList(page,searchedUserId);
        return PageResult.create(result);
    }
}
