package xyz.nanian.owl.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.user.domain.dto.AddressCreateDTO;
import xyz.nanian.owl.user.domain.dto.AddressUpdateDTO;
import xyz.nanian.owl.user.domain.vo.AddressVO;
import xyz.nanian.owl.user.service.UserAddressService;

import java.util.List;

/**
 * [UPGRADE] 用户收货地址前端控制器。
 * [TO_BE_DELETED] 旧映射 /user-address-do 已废弃。
 */
@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
@Tag(name = "用户地址管理", description = "收货地址 CRUD 与默认地址")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @GetMapping
    @Operation(summary = "地址列表")
    public Result<List<AddressVO>> list() {
        return Result.success(userAddressService.listAddresses());
    }

    @GetMapping("/{id}")
    @Operation(summary = "地址详情")
    public Result<AddressVO> detail(@PathVariable Long id) {
        return Result.success(userAddressService.getAddress(id));
    }

    @PostMapping
    @Operation(summary = "新增地址")
    public Result<Long> create(@RequestBody @Validated AddressCreateDTO addressCreateDTO) {
        return Result.success(userAddressService.createAddress(addressCreateDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新地址")
    public Result<ResultStatus> update(@PathVariable Long id,
                                       @RequestBody @Validated AddressUpdateDTO addressUpdateDTO) {
        if (userAddressService.updateAddress(id, addressUpdateDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除地址")
    public Result<ResultStatus> delete(@PathVariable Long id) {
        if (userAddressService.deleteAddress(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    @PutMapping("/{id}/default")
    @Operation(summary = "设为默认地址")
    public Result<ResultStatus> setDefault(@PathVariable Long id) {
        if (userAddressService.setDefaultAddress(id)) {
            return Result.success();
        }
        return Result.fail();
    }
}
