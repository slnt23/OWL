package xyz.nanian.owl.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;
import xyz.nanian.owl.admin.service.FeatureService;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

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
@RequestMapping("/admin/feature")
@RequiredArgsConstructor
@Tag(name = "前台特性管理")
public class FeatureController {

    private final FeatureService featureService;

    /**
     * 获取全部产品特性，按 sort_order 升序排列
     */
    @GetMapping
    @Operation(summary = "获取特性列表",description = "这里只获取四个")
    public Result<List<FeatureVO>> list() {
        List<FeatureVO> list = featureService.listByOrder();
        return Result.success(list);
    }

    /**
     * 根据 id 获取单条特性
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取单挑特性")
    public Result<FeatureVO> getById(@PathVariable Integer id) {
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
    public Result<ResultStatus> update(@PathVariable Integer id,
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
    public Result<ResultStatus> delete(@PathVariable Integer id) {
        Boolean result = featureService.deleteById(id);
        if(result){
            return Result.success();
        }else {
            return Result.fail();
        }
    }
}
