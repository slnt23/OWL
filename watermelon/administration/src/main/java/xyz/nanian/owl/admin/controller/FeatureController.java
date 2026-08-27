package xyz.nanian.owl.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;
import xyz.nanian.owl.admin.service.FeatureService;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;

import java.util.List;

/**
 * <p>
 * 产品特性展示表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@RestController
@RequestMapping("/api/admin/feature")
@RequiredArgsConstructor
@Tag(name = "前台特性管理")
public class FeatureController {

    private final FeatureService featureService;

    /**
     * 获取前台产品特性，按 sort_order 升序排列，这个是为前端展示使用的，只要4个，
     */
    @GetMapping
    @Operation(summary = "获取前台特性列表",description = "这里只获取四个")
    public Result<List<FeatureVO>> list() {
        List<FeatureVO> list = featureService.listByOrder();
        return Result.success(list);
    }

    /**
     * 分页获取全部特性，按 sort_order 升序排列
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取全部特性")
    public Result<ResultPage<FeatureVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.success(featureService.page(pageNum, pageSize));
    }

    /**
     * 根据 id 获取单条特性
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取单条特性")
    public Result<FeatureVO> getById(@PathVariable Long id) {
        FeatureVO feature = featureService.getById(id);
        return Result.success(feature);
    }

    /**
     * 新增产品特性
     */
    @PostMapping
    @Operation(summary = "新增产品特性")
    public Result<Integer> create(@Valid @RequestBody FeatureDTO dto) {
        Integer result = featureService.create(dto);
        return Result.success(result);
    }

    /**
     * 修改产品特性
     */
    @PutMapping("/{id}")
    @Operation(summary = "修改产品特性")
    public Result<ResultStatus> update(@PathVariable Long id,
                                       @Valid @RequestBody FeatureDTO dto) {
        dto.setId(id);
        Boolean result = featureService.update(dto);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    /**
     * 删除产品特性
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除产品特性")
    public Result<ResultStatus> delete(@PathVariable Long id) {
        Boolean result = featureService.deleteById(id);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }
}