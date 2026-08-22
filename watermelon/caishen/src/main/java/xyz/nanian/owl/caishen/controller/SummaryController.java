package xyz.nanian.owl.caishen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.caishen.constant.PeriodType;
import xyz.nanian.owl.caishen.domain.dto.SummaryQueryDTO;
import xyz.nanian.owl.caishen.domain.dto.SummaryTriggerDTO;
import xyz.nanian.owl.caishen.domain.vo.SummaryVO;
import xyz.nanian.owl.caishen.service.SummaryService;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;

/**
 * 总结查询与手动触发接口。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@RestController
@RequestMapping("/api/caishen/summaries")
@RequiredArgsConstructor
@Tag(name = "区间总结")
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping
    @Operation(summary = "我的总结列表（分页）")
    public Result<ResultPage<SummaryVO>> page(SummaryQueryDTO query) {
        return Result.success(summaryService.pageMy(query));
    }

    @GetMapping("/{id}")
    @Operation(summary = "总结详情")
    public Result<SummaryVO> detail(@PathVariable Long id) {
        return Result.success(summaryService.getById(id));
    }

    @PostMapping
    @Operation(summary = "手动触发生成总结")
    public Result<Long> trigger(@Valid @RequestBody SummaryTriggerDTO dto) {
        PeriodType periodType;
        try {
            periodType = PeriodType.fromName(dto.getPeriodType());
        } catch (IllegalArgumentException e) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
        Long userId = CurrentUserContext.getUserId();
        if (userId == null) {
            throw new BizException(ResultStatus.UNAUTHORIZED);
        }
        return Result.success(summaryService.generateForUser(userId, periodType));
    }
}
