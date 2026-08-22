package xyz.nanian.owl.caishen.service;

import xyz.nanian.owl.caishen.domain.dto.FundWatchCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundWatchUpdateDTO;
import xyz.nanian.owl.caishen.domain.vo.FundWatchVO;

import java.util.List;

/**
 * 用户关注基金管理，按 user_id 隔离。
 *
 * @author slnt23
 * @since 2026/8/23
 */
public interface WatchService {

    /**
     * 我的关注列表（含最新净值、提醒规则数）。
     */
    List<FundWatchVO> listMyWatches();

    /**
     * 添加关注，返回 watchId。
     */
    Long create(FundWatchCreateDTO dto);

    /**
     * 取消关注（级联删除关联提醒规则）。
     */
    void delete(Long id);

    /**
     * 更新备注。
     */
    void updateRemark(Long id, FundWatchUpdateDTO dto);
}
