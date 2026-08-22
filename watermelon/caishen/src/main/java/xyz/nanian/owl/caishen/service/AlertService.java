package xyz.nanian.owl.caishen.service;

import xyz.nanian.owl.caishen.domain.dto.FundAlertCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundAlertUpdateDTO;
import xyz.nanian.owl.caishen.domain.vo.FundAlertVO;

import java.time.LocalDate;
import java.util.List;

/**
 * 提醒规则 CRUD + 阈值检查 + 邮件触发。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface AlertService {

    /**
     * 某关注的全部提醒规则。
     */
    List<FundAlertVO> listByWatch(Long watchId);

    /**
     * 新增提醒规则，返回 alertId。
     */
    Long create(Long watchId, FundAlertCreateDTO dto);

    /**
     * 更新提醒规则（阈值、状态）。
     */
    void update(Long id, FundAlertUpdateDTO dto);

    /**
     * 删除提醒规则。
     */
    void delete(Long id);

    /**
     * 重置为 ACTIVE（清空最近触发时间，继续监控）。
     */
    void reset(Long id);

    /**
     * 遍历 ACTIVE 规则检查阈值，触及则发邮件并置 TRIGGERED。
     *
     * @param today 判定日期（同一天不重复触发）
     */
    void checkAndTrigger(LocalDate today);
}
