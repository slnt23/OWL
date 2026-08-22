package xyz.nanian.owl.caishen.service;

import xyz.nanian.owl.caishen.constant.PeriodType;
import xyz.nanian.owl.caishen.domain.dto.SummaryQueryDTO;
import xyz.nanian.owl.caishen.domain.vo.SummaryVO;
import xyz.nanian.owl.common.result.ResultPage;

/**
 * 总结生成与查询。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface SummaryService {

    /**
     * 我的总结分页。
     */
    ResultPage<SummaryVO> pageMy(SummaryQueryDTO query);

    /**
     * 总结详情（校验归属）。
     */
    SummaryVO getById(Long id);

    /**
     * 为指定用户生成周期总结，返回 summaryId。
     * 状态机：PENDING → PROCESSING → SUCCESS / FAILED。
     */
    Long generateForUser(Long userId, PeriodType periodType);

    /**
     * 为所有有关注基金的用户生成总结（逐用户隔离失败）。
     */
    void generateForAllUsers(PeriodType periodType);
}
