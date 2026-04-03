package xyz.nanian.owl.pitaya.merchant.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.vo.OrderListVO;

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

    /**
     * 查询用户订单列表
     * @param page
     * @param userId
     * @return
     */
    IPage<OrderListVO> pageOrderList(Page<?> page,
                                     @Param("userId") Long userId);

}
