package xyz.nanian.owl.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;
import xyz.nanian.owl.admin.service.SpotlightService;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

import java.util.List;

/**
 * <p>
 * 首页焦点展示项目表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@RestController
@RequestMapping("/admin/spotlight")
@RequiredArgsConstructor
@Tag(name= "焦点项目管理")
public class SpotlightController {

    private final SpotlightService spotlightService;

    /** 获取全部焦点项目，按 order 升序排列 */
    @GetMapping
    @Operation(summary = "获取全部焦点特性")
    public Result<List<SpotlightVO>> list() {
        List<SpotlightVO> list = spotlightService.listByOrder();
        return Result.success(list);
    }

    /** 根据 id 获取单条 */
    @Operation(summary = "获取单条焦点特性")
    @GetMapping("/{id}")
    public Result<SpotlightVO> getById(@PathVariable Integer id) {
        SpotlightVO spotlightVO = spotlightService.getById(id);
        return Result.success(spotlightVO);
    }

    /** 新增焦点项目 */
    @PostMapping
    @Operation(summary = "新增焦点")
    public Result<Integer> create(@Valid @RequestBody SpotlightDTO dto) {
        int result= spotlightService.create(dto);

        return Result.success(result);
    }

    /** 修改焦点项目 */
    @PutMapping("/{id}")
    @Operation(summary = "更新焦点")
    public Result<ResultStatus> update(@Valid @RequestBody SpotlightDTO dto) {
//        dto.setId(id);  // 确保路径 id 与 body 一致
        Boolean result = spotlightService.update(dto);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    /** 删除焦点项目 */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除焦点")
    public Result<Void> delete(@PathVariable Integer id) {
        Boolean result = spotlightService.deleteById(id);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }
}
