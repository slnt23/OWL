package xyz.nanian.owl.admin.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.RoleCreateDTO;
import xyz.nanian.owl.admin.domain.dto.RoleUpdateDTO;
import xyz.nanian.owl.admin.domain.vo.RoleVO;
import xyz.nanian.owl.admin.service.RoleAdminService;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultStatus;

import java.util.List;

/**
 * 后台角色管理：列表、详情、新增、更新、删除、启停。
 *
 * @author slnt23
 * @since 2026/8/14
 */
@RestController
@RequestMapping("/api/admin/role-do")
@RequiredArgsConstructor
@Tag(name = "后台角色管理")
public class RoleAdminController {

    private final RoleAdminService roleAdminService;

    @GetMapping
    @Operation(summary = "角色列表")
    public Result<List<RoleVO>> list() {
        return Result.success(roleAdminService.listAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "角色详情")
    public Result<RoleVO> getById(@PathVariable Long id) {
        return Result.success(roleAdminService.getById(id));
    }

    @PostMapping
    @Operation(summary = "新增角色")
    public Result<Long> create(@Valid @RequestBody RoleCreateDTO createDTO) {
        return Result.success(roleAdminService.create(createDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新角色")
    public Result<ResultStatus> update(@PathVariable Long id,
                                       @Valid @RequestBody RoleUpdateDTO updateDTO) {
        if (roleAdminService.update(id, updateDTO)) {
            return Result.success();
        }
        return Result.fail();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Result<ResultStatus> delete(@PathVariable Long id) {
        if (roleAdminService.deleteById(id)) {
            return Result.success();
        }
        return Result.fail();
    }

    @PutMapping("/{id}/enabled")
    @Operation(summary = "启用/禁用角色")
    public Result<ResultStatus> updateEnabled(@PathVariable Long id,
                                              @RequestParam Boolean enabled) {
        if (roleAdminService.updateEnabled(id, enabled)) {
            return Result.success();
        }
        return Result.fail();
    }

}
