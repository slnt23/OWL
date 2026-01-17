package xyz.nanian.owl.pitaya.merchant.service.impl;


import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.merchant.mapper.MerOrderMapper;
import xyz.nanian.owl.pitaya.merchant.service.MerOrderService;

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
}
