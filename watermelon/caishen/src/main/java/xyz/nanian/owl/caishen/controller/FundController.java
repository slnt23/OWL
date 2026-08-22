package xyz.nanian.owl.caishen.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.caishen.domain.dto.FundNavPageQueryDTO;
import xyz.nanian.owl.caishen.domain.vo.FundNavVO;
import xyz.nanian.owl.caishen.domain.vo.FundVO;
import xyz.nanian.owl.caishen.service.FundService;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;

import java.util.List;

/**
 * 基金档案与净值查询接口。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@RestController
@RequestMapping("/api/caishen/funds")
@RequiredArgsConstructor
@Tag(name = "基金档案与净值")
public class FundController {

    private final FundService fundService;

    @GetMapping
    @Operation(summary = "搜索基金（按代码或名称模糊）")
    public Result<List<FundVO>> search(@RequestParam(required = false) String keyword) {
        return Result.success(fundService.search(keyword));
    }

    @GetMapping("/{fundCode}")
    @Operation(summary = "基金详情")
    public Result<FundVO> detail(@PathVariable String fundCode) {
        return Result.success(fundService.getByFundCode(fundCode));
    }

    @GetMapping("/{fundCode}/nav")
    @Operation(summary = "净值历史分页")
    public Result<ResultPage<FundNavVO>> pageNav(@PathVariable String fundCode, FundNavPageQueryDTO query) {
        return Result.success(fundService.pageNav(fundCode, query));
    }
}
