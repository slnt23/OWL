package xyz.nanian.owl.sugarcane.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.sugarcane.domain.dto.*;
import xyz.nanian.owl.sugarcane.domain.vo.PriceCompareVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceLatestVO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceTrendVO;
import xyz.nanian.owl.sugarcane.domain.vo.SourceCompareVO;

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
public class RecordController {

    /**
     * 1. 查询最新价格
     */
    @PostMapping("/latest")
    public List<PriceLatestVO> latest(
            @RequestBody PriceLatestQueryDTO dto) {
//        return priceService.queryLatest(dto);
        return null;
    }

    /**
     * 2. 查询趋势
     */
    @PostMapping("/trend")
    public PriceTrendVO trend(
            @RequestBody PriceTrendQueryDTO dto) {
//        return priceService.queryTrend(dto);
        return null;
    }

    /**
     * 3. 多地区对比
     */
    @PostMapping("/compare/location")
    public PriceCompareVO compareLocation(
            @RequestBody PriceCompareLocationDTO dto) {
//        return priceService.compareLocation(dto);
        return null;
    }

    /**
     * 4. 多来源对比
     */
    @PostMapping("/compare/source")
    public List<SourceCompareVO> compareSource(
            @RequestBody PriceCompareSourceDTO dto) {
//        return priceService.compareSource(dto);
        return null;
    }

    /**
     * 5. 原始记录分页
     */
//    @PostMapping("/page")
//    public PageResult<PriceRecordVO> page(
//            @RequestBody PricePageQueryDTO dto) {
//        return priceService.pageQuery(dto);
//    }

    /**
     * 6. 写入价格数据,（后台/爬虫）
     * 可以设计成爬虫的开关，
     */
    @PostMapping
    public void create(
            @RequestBody PriceRecordCreateDTO dto) {
//        priceService.create(dto);
//        return null;
    }
}

