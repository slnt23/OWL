package xyz.nanian.owl.caishen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.caishen.domain.dto.FundWatchCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundWatchUpdateDTO;
import xyz.nanian.owl.caishen.domain.vo.FundWatchVO;
import xyz.nanian.owl.caishen.service.WatchService;
import xyz.nanian.owl.common.result.Result;

import java.util.List;

/**
 * 关注管理接口。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@RestController
@RequestMapping("/api/caishen/watches")
@RequiredArgsConstructor
@Tag(name = "关注管理")
public class WatchController {

    private final WatchService watchService;

    @GetMapping
    @Operation(summary = "我的关注列表（含最新净值）")
    public Result<List<FundWatchVO>> list() {
        return Result.success(watchService.listMyWatches());
    }

    @PostMapping
    @Operation(summary = "添加关注")
    public Result<Long> create(@Valid @RequestBody FundWatchCreateDTO dto) {
        return Result.success(watchService.create(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "取消关注（级联删除提醒规则）")
    public Result<Void> delete(@PathVariable Long id) {
        watchService.delete(id);
        return Result.success();
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新备注")
    public Result<Void> updateRemark(@PathVariable Long id, @Valid @RequestBody FundWatchUpdateDTO dto) {
        watchService.updateRemark(id, dto);
        return Result.success();
    }
}
