package xyz.nanian.owl.pitaya.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.pitaya.domain.dto.OrderDTO;
import xyz.nanian.owl.pitaya.domain.entity.OrderDO;
import xyz.nanian.owl.pitaya.domain.entity.OrderDetailDO;
import xyz.nanian.owl.pitaya.domain.entity.UserAddressDO;
import xyz.nanian.owl.pitaya.domain.vo.AddressVO;
import xyz.nanian.owl.pitaya.domain.vo.OrderDetailVO;
import xyz.nanian.owl.pitaya.domain.vo.OrderItemVO;

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
