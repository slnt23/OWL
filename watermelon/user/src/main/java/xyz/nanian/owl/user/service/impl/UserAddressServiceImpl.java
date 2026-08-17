package xyz.nanian.owl.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.common.exception.LoginFailureException;
import xyz.nanian.owl.user.domain.dto.AddressCreateDTO;
import xyz.nanian.owl.user.domain.dto.AddressUpdateDTO;
import xyz.nanian.owl.user.domain.entity.UserAddressDO;
import xyz.nanian.owl.user.domain.vo.AddressVO;
import xyz.nanian.owl.user.mapper.UserAddressMapper;
import xyz.nanian.owl.user.mapstruct.AddressConvert;
import xyz.nanian.owl.user.service.UserAddressService;

import java.util.List;
import java.util.Objects;

/**
 * [UPGRADE] 用户收货地址服务实现。
 */
@Service
@RequiredArgsConstructor
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddressDO>
        implements UserAddressService {

    private final AddressConvert addressConvert;

    @Override
    public List<AddressVO> listAddresses() {
        Long userId = CurrentUserContext.getUserId();
        LambdaQueryWrapper<UserAddressDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserAddressDO::getUserId, userId)
                .orderByDesc(UserAddressDO::getIsDefault)
                .orderByAsc(UserAddressDO::getId);
        return addressConvert.toVOList(list(wrapper));
    }

    @Override
    public AddressVO getAddress(Long id) {
        return getOwned(CurrentUserContext.getUserId(), id);
    }

    @Override
    public AddressVO getOwned(Long userId, Long addressId) {
        LambdaQueryWrapper<UserAddressDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserAddressDO::getId, addressId)
                .eq(UserAddressDO::getUserId, userId);
        UserAddressDO addressDO = getOne(wrapper);
        if (addressDO == null) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        return addressConvert.toVO(addressDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createAddress(AddressCreateDTO addressCreateDTO) {
        Long userId = CurrentUserContext.getUserId();
        UserAddressDO addressDO = new UserAddressDO();
        addressDO.setReceiverName(addressCreateDTO.getReceiverName());
        addressDO.setReceiverPhone(addressCreateDTO.getReceiverPhone());
        addressDO.setProvince(addressCreateDTO.getProvince());
        addressDO.setCity(addressCreateDTO.getCity());
        addressDO.setDistrict(addressCreateDTO.getDistrict());
        addressDO.setDetail(addressCreateDTO.getDetail());
        addressDO.setUserId(userId);

        boolean defaultAddress = Boolean.TRUE.equals(addressCreateDTO.getIsDefault())
                || count(Wrappers.<UserAddressDO>lambdaQuery().eq(UserAddressDO::getUserId, userId)) == 0;
        if (defaultAddress) {
            clearDefaults(userId);
        }
        addressDO.setIsDefault(defaultAddress ? 1 : 0);
        save(addressDO);
        return addressDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateAddress(Long id, AddressUpdateDTO addressUpdateDTO) {
        Long userId = CurrentUserContext.getUserId();
        UserAddressDO addressDO = getOwnedEntity(userId, id);

        addressDO.setReceiverName(addressUpdateDTO.getReceiverName());
        addressDO.setReceiverPhone(addressUpdateDTO.getReceiverPhone());
        addressDO.setProvince(addressUpdateDTO.getProvince());
        addressDO.setCity(addressUpdateDTO.getCity());
        addressDO.setDistrict(addressUpdateDTO.getDistrict());
        addressDO.setDetail(addressUpdateDTO.getDetail());

        if (Boolean.TRUE.equals(addressUpdateDTO.getIsDefault())) {
            clearDefaults(userId);
            addressDO.setIsDefault(1);
        } else {
            addressDO.setIsDefault(addressDO.getIsDefault() == null ? 0 : addressDO.getIsDefault());
        }
        return updateById(addressDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteAddress(Long id) {
        Long userId = CurrentUserContext.getUserId();
        UserAddressDO addressDO = getOwnedEntity(userId, id);
        boolean deletedDefault = Objects.equals(addressDO.getIsDefault(), 1);
        boolean deleted = removeById(id);
        if (deleted && deletedDefault) {
            LambdaQueryWrapper<UserAddressDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserAddressDO::getUserId, userId)
                    .orderByAsc(UserAddressDO::getCreateTime)
                    .orderByAsc(UserAddressDO::getId)
                    .last("LIMIT 1");
            UserAddressDO next = getOne(wrapper);
            if (next != null) {
                next.setIsDefault(1);
                updateById(next);
            }
        }
        return deleted;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean setDefaultAddress(Long id) {
        Long userId = CurrentUserContext.getUserId();
        getOwnedEntity(userId, id);
        clearDefaults(userId);

        LambdaUpdateWrapper<UserAddressDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAddressDO::getId, id)
                .eq(UserAddressDO::getUserId, userId)
                .set(UserAddressDO::getIsDefault, 1);
        return update(null, wrapper);
    }

    private UserAddressDO getOwnedEntity(Long userId, Long addressId) {
        LambdaQueryWrapper<UserAddressDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserAddressDO::getId, addressId)
                .eq(UserAddressDO::getUserId, userId);
        UserAddressDO addressDO = getOne(wrapper);
        if (addressDO == null) {
            throw new LoginFailureException(ResultStatus.NOT_FOUND);
        }
        return addressDO;
    }

    private void clearDefaults(Long userId) {
        LambdaUpdateWrapper<UserAddressDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserAddressDO::getUserId, userId)
                .set(UserAddressDO::getIsDefault, 0);
        update(null, wrapper);
    }
}
