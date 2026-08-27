package xyz.nanian.owl.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;
import xyz.nanian.owl.admin.service.SpotlightService;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;

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
@RequestMapping("/api/admin/spotlight")
@RequiredArgsConstructor
@Tag(name= "焦点项目管理")
public class SpotlightController {

    private final SpotlightService spotlightService;

    /**
     * 获取前台焦点项目，按 sort_order 升序排列
     * 这个是为前端展示使用的，只要4个 */
    @GetMapping
    @Operation(summary = "获取前台焦点特性",description = "这里只获取4个")
    public Result<List<SpotlightVO>> list() {
        List<SpotlightVO> list = spotlightService.listByOrder();
        return Result.success(list);
    }

    /**
     * 分页获取全部焦点项目，按 sort_order 升序排列
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取全部焦点")
    public Result<ResultPage<SpotlightVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.success(spotlightService.page(pageNum, pageSize));
    }

    /** 根据 id 获取单条 */
    @Operation(summary = "获取单条焦点特性")
    @GetMapping("/{id}")
    public Result<SpotlightVO> getById(@PathVariable Long id) {
        SpotlightVO spotlightVO = spotlightService.getById(id);
        return Result.success(spotlightVO);
    }

    /** 新增焦点项目 */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "新增焦点")
    public Result<Integer> create(@Valid @ModelAttribute SpotlightDTO dto) {
        int result= spotlightService.create(dto);
        return Result.success(result);
    }

    /** 修改焦点项目 */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "更新焦点")
    public Result<ResultStatus> update(
            @PathVariable Long id,
            @Valid @ModelAttribute SpotlightDTO dto) {
        dto.setId(id);
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
    public Result<Void> delete(@PathVariable Long id) {
        Boolean result = spotlightService.deleteById(id);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }
}