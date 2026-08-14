package xyz.nanian.owl.user.service;

import xyz.nanian.owl.user.domain.entity.UserAddressDO;
import xyz.nanian.owl.user.domain.dto.AddressCreateDTO;
import xyz.nanian.owl.user.domain.dto.AddressUpdateDTO;
import xyz.nanian.owl.user.domain.vo.AddressVO;
import com.baomidou.mybatisplus.spring.service.IService;

import java.util.List;

/**
 * <p>
 * 用户收货地址表 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-19 13:02:36
 */
public interface UserAddressService extends IService<UserAddressDO> {

    /**
     * [UPGRADE] 当前用户地址列表。
     */
    List<AddressVO> listAddresses();

    /**
     * [UPGRADE] 当前用户单个地址详情。
     */
    AddressVO getAddress(Long id);

    /**
     * [UPGRADE] 校验地址归属后返回，供 pitaya 等模块复用。
     */
    AddressVO getOwned(Long userId, Long addressId);

    /**
     * [UPGRADE] 新增地址。
     */
    Long createAddress(AddressCreateDTO addressCreateDTO);

    /**
     * [UPGRADE] 更新地址。
     */
    Boolean updateAddress(Long id, AddressUpdateDTO addressUpdateDTO);

    /**
     * [UPGRADE] 删除地址，默认地址被删除时自动提升最早地址。
     */
    Boolean deleteAddress(Long id);

    /**
     * [UPGRADE] 设为默认地址。
     */
    Boolean setDefaultAddress(Long id);

}
