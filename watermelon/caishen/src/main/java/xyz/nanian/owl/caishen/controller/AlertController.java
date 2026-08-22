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
import xyz.nanian.owl.caishen.domain.dto.FundAlertCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundAlertUpdateDTO;
import xyz.nanian.owl.caishen.domain.vo.FundAlertVO;
import xyz.nanian.owl.caishen.service.AlertService;
import xyz.nanian.owl.common.result.Result;

import java.util.List;

/**
 * 提醒规则接口。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@RestController
@RequestMapping("/api/caishen")
@RequiredArgsConstructor
@Tag(name = "提醒规则")
public class AlertController {

    private final AlertService alertService;

    @GetMapping("/watches/{watchId}/alerts")
    @Operation(summary = "某关注的全部提醒规则")
    public Result<List<FundAlertVO>> listByWatch(@PathVariable Long watchId) {
        return Result.success(alertService.listByWatch(watchId));
    }

    @PostMapping("/watches/{watchId}/alerts")
    @Operation(summary = "新增提醒规则")
    public Result<Long> create(@PathVariable Long watchId, @Valid @RequestBody FundAlertCreateDTO dto) {
        return Result.success(alertService.create(watchId, dto));
    }

    @PutMapping("/alerts/{id}")
    @Operation(summary = "更新提醒规则（阈值、状态）")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody FundAlertUpdateDTO dto) {
        alertService.update(id, dto);
        return Result.success();
    }

    @DeleteMapping("/alerts/{id}")
    @Operation(summary = "删除提醒规则")
    public Result<Void> delete(@PathVariable Long id) {
        alertService.delete(id);
        return Result.success();
    }

    @PutMapping("/alerts/{id}/reset")
    @Operation(summary = "重置为 ACTIVE（继续监控）")
    public Result<Void> reset(@PathVariable Long id) {
        alertService.reset(id);
        return Result.success();
    }
}
