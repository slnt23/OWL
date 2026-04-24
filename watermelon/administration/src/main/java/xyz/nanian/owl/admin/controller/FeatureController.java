package xyz.nanian.owl.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;
import xyz.nanian.owl.admin.service.FeatureService;
import xyz.nanian.owl.result.Result;

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
public class FeatureController {

    private final FeatureService featureService;

    /**
     * 获取全部产品特性，按 sort_order 升序排列
     */
    @GetMapping
    public Result<List<FeatureVO>> list() {
        List<FeatureVO> list = featureService.listByOrder();
        return Result.success(list);
    }

    /**
     * 根据 id 获取单条特性
     */
    @GetMapping("/{id}")
    public Result<FeatureVO> getById(@PathVariable Integer id) {
        FeatureVO feature = featureService.getById(id);
        return Result.success();
    }

    /**
     * 新增产品特性
     */
    @PostMapping
    public Result<FeatureVO> create(@Valid @RequestBody FeatureDTO dto) {
        FeatureVO feature = featureService.create(dto);
        return Result.success();
    }

    /**
     * 修改产品特性
     */
    @PutMapping("/{id}")
    public Result<FeatureVO> update(@PathVariable Integer id,
                                    @Valid @RequestBody FeatureDTO dto) {
        dto.setId(id);
        FeatureVO feature = featureService.update(dto);
        return Result.success();
    }

    /**
     * 删除产品特性
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Integer id) {
        featureService.deleteById(id);
        return Result.success();
    }
}
