package xyz.nanian.owl.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.UserCreateDTO;
import xyz.nanian.owl.admin.domain.dto.UserPasswordResetDTO;
import xyz.nanian.owl.admin.domain.dto.UserUpdateDTO;
import xyz.nanian.owl.admin.domain.vo.AdminUserVO;
import xyz.nanian.owl.admin.service.UserAdminService;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;

/**
 * 后台用户管理：分页/搜索、新增、修改、删除、封禁、角色变更、重置密码。
 *
 * @author slnt23
 * @since 2026/8/14
 */
@RestController
@RequestMapping("/api/admin/user-do")
@RequiredArgsConstructor
@Tag(name = "后台用户管理")
public class UserAdminController {

    private final UserAdminService userAdminService;

    /**
     * 分页获取用户信息，支持关键字搜索和状态/角色筛选。
     */
    @GetMapping
    @Operation(summary = "用户分页列表")
    public Result<ResultPage<AdminUserVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Byte status,
            @RequestParam(required = false) Long roleId) {
        return Result.success(userAdminService.page(pageNum, pageSize, keyword, status, roleId));
    }

    /**
     * 获取用户详情。
     */
    @GetMapping("/{id}")
    @Operation(summary = "用户详情")
    public Result<AdminUserVO> getById(@PathVariable Long id) {
        return Result.success(userAdminService.getById(id));
    }

    /**
     * 新增用户。
     */
    @PostMapping
    @Operation(summary = "新增用户")
    public Result<Long> create(@Valid @RequestBody UserCreateDTO createDTO) {
        return Result.success(userAdminService.create(createDTO));
    }

    /**
     * 更新用户基础资料、状态或角色。
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新用户")
    public Result<ResultStatus> update(@PathVariable Long id,
                                       @Valid @RequestBody UserUpdateDTO updateDTO) {
        if (userAdminService.update(id, updateDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 删除用户。
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除用户")
    public Result<ResultStatus> delete(@PathVariable Long id) {
        if (userAdminService.deleteById(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 封禁/解封用户，status：0=正常，1=封禁。
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "封禁/解封用户")
    public Result<ResultStatus> updateStatus(@PathVariable Long id,
                                             @RequestParam Byte status) {
        if (userAdminService.updateStatus(id, status)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 修改用户角色，可用于设置为管理员。
     */
    @PutMapping("/{id}/role")
    @Operation(summary = "修改用户角色")
    public Result<ResultStatus> updateRole(@PathVariable Long id,
                                           @RequestParam Long roleId) {
        if (userAdminService.updateRole(id, roleId)) {
            return Result.success();
        }
        return Result.fail();
    }

    /**
     * 重置用户密码，成功后旧 token 全部失效。
     */
    @PutMapping("/{id}/password/reset")
    @Operation(summary = "重置用户密码")
    public Result<ResultStatus> resetPassword(@PathVariable Long id,
                                              @Valid @RequestBody UserPasswordResetDTO resetDTO) {
        if (userAdminService.resetPassword(id, resetDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

}
