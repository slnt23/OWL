package xyz.nanian.owl.caishen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.caishen.domain.dto.FundNavPageQueryDTO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundNavDO;
import xyz.nanian.owl.caishen.domain.vo.FundNavVO;
import xyz.nanian.owl.caishen.domain.vo.FundVO;
import xyz.nanian.owl.caishen.mapper.CaishenFundMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundNavMapper;
import xyz.nanian.owl.caishen.mapstruct.CaishenFundConvert;
import xyz.nanian.owl.caishen.service.FundService;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;

import java.util.List;

/**
 * 基金档案与净值查询实现。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private static final int MAX_PAGE_SIZE = 100;

    private final CaishenFundMapper caishenFundMapper;
    private final CaishenFundNavMapper caishenFundNavMapper;
    private final CaishenFundConvert caishenFundConvert;

    @Override
    public List<FundVO> search(String keyword) {
        LambdaQueryWrapper<CaishenFundDO> wrapper = Wrappers.<CaishenFundDO>lambdaQuery()
                .eq(CaishenFundDO::getStatus, 1);
        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();
            wrapper.and(w -> w.like(CaishenFundDO::getFundCode, kw)
                    .or().like(CaishenFundDO::getFundName, kw));
        }
        wrapper.orderByAsc(CaishenFundDO::getFundCode);
        return caishenFundConvert.toVOList(caishenFundMapper.selectList(wrapper));
    }

    @Override
    public FundVO getByFundCode(String fundCode) {
        CaishenFundDO fund = caishenFundMapper.selectOne(
                Wrappers.<CaishenFundDO>lambdaQuery().eq(CaishenFundDO::getFundCode, fundCode));
        if (fund == null) {
            throw new BizException(ResultStatus.DATA_NOT_EXIST);
        }
        return caishenFundConvert.toVO(fund);
    }

    @Override
    public ResultPage<FundNavVO> pageNav(String fundCode, FundNavPageQueryDTO query) {
        requireFundExists(fundCode);

        int pageNum = query.getPageNum() == null || query.getPageNum() <= 0 ? 1 : query.getPageNum();
        int pageSize = query.getPageSize() == null || query.getPageSize() <= 0
                ? 10 : Math.min(query.getPageSize(), MAX_PAGE_SIZE);

        LambdaQueryWrapper<CaishenFundNavDO> wrapper = Wrappers.<CaishenFundNavDO>lambdaQuery()
                .eq(CaishenFundNavDO::getFundCode, fundCode);
        if (query.getStartDate() != null) {
            wrapper.ge(CaishenFundNavDO::getNavDate, query.getStartDate());
        }
        if (query.getEndDate() != null) {
            wrapper.le(CaishenFundNavDO::getNavDate, query.getEndDate());
        }
        wrapper.orderByDesc(CaishenFundNavDO::getNavDate);

        Page<CaishenFundNavDO> page = new Page<>(pageNum, pageSize);
        IPage<CaishenFundNavDO> result = caishenFundNavMapper.selectPage(page, wrapper);

        List<FundNavVO> records = caishenFundConvert.toNavVOList(result.getRecords());
        ResultPage<FundNavVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(records);
        return pageResult;
    }

    private void requireFundExists(String fundCode) {
        Long count = caishenFundMapper.selectCount(
                Wrappers.<CaishenFundDO>lambdaQuery().eq(CaishenFundDO::getFundCode, fundCode));
        if (count == null || count == 0) {
            throw new BizException(ResultStatus.DATA_NOT_EXIST);
        }
    }
}
