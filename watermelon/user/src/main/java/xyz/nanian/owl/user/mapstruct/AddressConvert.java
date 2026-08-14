package xyz.nanian.owl.user.mapstruct;

import org.mapstruct.Mapper;
import xyz.nanian.owl.user.domain.entity.UserAddressDO;
import xyz.nanian.owl.user.domain.vo.AddressVO;

import java.util.List;

/**
 * [UPGRADE] 地址实体转换器。
 */
@Mapper(componentModel = "spring")
public interface AddressConvert {

    AddressVO toVO(UserAddressDO addressDO);

    List<AddressVO> toVOList(List<UserAddressDO> addressDOList);
}
