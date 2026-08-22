package xyz.nanian.owl.caishen.service;

import xyz.nanian.owl.caishen.domain.dto.FundNavPageQueryDTO;
import xyz.nanian.owl.caishen.domain.vo.FundNavVO;
import xyz.nanian.owl.caishen.domain.vo.FundVO;
import xyz.nanian.owl.common.result.ResultPage;

import java.util.List;

/**
 * 基金档案与净值查询。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface FundService {

    /**
     * 按代码或名称模糊搜索正常状态的基金。
     */
    List<FundVO> search(String keyword);

    /**
     * 基金详情。
     */
    FundVO getByFundCode(String fundCode);

    /**
     * 净值历史分页。
     */
    ResultPage<FundNavVO> pageNav(String fundCode, FundNavPageQueryDTO query);
}
