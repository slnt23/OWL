package xyz.nanian.owl.sugarcane.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.sugarcane.domain.dto.*;
import xyz.nanian.owl.sugarcane.domain.vo.PriceCompareVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceLatestVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceTrendVO;
import xyz.nanian.owl.sugarcane.domain.vo.SourceCompareVO;
import xyz.nanian.owl.sugarcane.service.RecordService;

import java.util.List;

/**
 * <p>
 * 价格记录表（时间序列数据） 前端控制器,复杂查询、高频调用、读多写少
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/price")
@Tag(name = "价格查询")
@RequiredArgsConstructor
public class RecordController {

    final RecordService recordService;

    /**
     * 1. 查询最新价格
     */
    @PostMapping("/latest")
    @Operation(summary = "查询最新价格")
    public Result<PriceLatestVO> latest(@RequestBody PriceLatestQueryDTO dto) {
        return Result.success(recordService.queryLatest(dto));
    }

    /**
     * 2. 查询趋势
     */
    @PostMapping("/trend")
    @Operation(summary = "查询趋势")
    public Result<List<PriceTrendVO>> trend(@RequestBody PriceTrendQueryDTO dto) {
        return Result.success(recordService.queryTrend(dto));
    }

    /**
     * 3. 多地区对比
     */
    @PostMapping("/compare/location")
    @Operation(summary = "多地区对比")
    public Result<PriceCompareVO> compareLocation(@RequestBody PriceCompareLocationDTO dto) {
        return Result.success(recordService.compareLocation(dto));
    }

    /**
     * 4. 多来源对比
     */
    @PostMapping("/compare/source")
    @Operation(summary = "多来源对比")
    public Result<List<SourceCompareVO>> compareSource(@RequestBody PriceCompareSourceDTO dto) {
        return Result.success(recordService.compareSource(dto));
    }

    /**
     * 5. 原始记录分页
     */
//    @PostMapping("/page")
//    @Operation(summary = "原始记录分页")
//    public PageResult<PriceRecordVO> page(@RequestBody PricePageQueryDTO dto) {
//        return priceService.pageQuery(dto);
//    }

    /**
     * 6. 写入价格数据,（后台/爬虫）
     * 可以设计成爬虫的开关，
     */
    @PostMapping
    @Operation(summary = "写入价格数据", description = "可以作为后台爬虫的开关")
    public void create(@RequestBody PriceRecordCreateDTO dto) {
//        priceService.create(dto);
//        return null;
    }
}

