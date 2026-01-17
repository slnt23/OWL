package xyz.nanian.owl.pitaya.mapstruct;


import com.baomidou.mybatisplus.core.metadata.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import xyz.nanian.owl.pitaya.dto.OrderDTO;
import xyz.nanian.owl.pitaya.entity.OrderDO;
import xyz.nanian.owl.pitaya.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.vo.AddressVO;
import xyz.nanian.owl.pitaya.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.vo.OrderItemVO;

import java.util.List;

/**
 * 订单Map
 *
 * @author slnt23
 * @since 2026/1/17
 */

@Mapper(componentModel = "spring")
public interface OrderConvert {

    OrderDO OrderDTOToOrderDO(OrderDTO orderDTO);

    OrderDetailDO OrderDTOToOrderDetailDO(OrderDTO orderDTO);

    OrderDetailVO OrderDOToOrderDetailVO(OrderDO orderDO);

    @Mapping(source = "district",target = "area")
    @Mapping(source = "detail",target = "detailAddress")
    AddressVO addressDOToAddressVO(UserAddressDO addressDO);

    OrderItemVO orderDetailDOToOrderItemVO(OrderDetailDO orderDetailDO);

    List<OrderItemVO> orderDetailDOToItemVOList(List<OrderDetailDO> orderDetailDO);

    OrderDetailDO orderItemToOrderDetail(OrderDTO.OrderItemCreateDTO orderItemCreateDTO);

    List<OrderDetailDO> orderItemToOrderDetailDOList(List<OrderDTO.OrderItemCreateDTO> orderItemCreateDTOList);

}
