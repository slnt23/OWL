package xyz.nanian.owl.caishen.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.caishen.domain.dto.FundWatchCreateDTO;
import xyz.nanian.owl.caishen.domain.dto.FundWatchUpdateDTO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundAlertDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundDO;
import xyz.nanian.owl.caishen.domain.entity.CaishenFundWatchDO;
import xyz.nanian.owl.caishen.domain.vo.FundWatchVO;
import xyz.nanian.owl.caishen.mapper.CaishenFundAlertMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundMapper;
import xyz.nanian.owl.caishen.mapper.CaishenFundWatchMapper;
import xyz.nanian.owl.caishen.service.WatchService;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.log.annotation.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户关注基金管理实现，按 user_id 隔离。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WatchServiceImpl implements WatchService {

    private final CaishenFundWatchMapper caishenFundWatchMapper;
    private final CaishenFundMapper caishenFundMapper;
    private final CaishenFundAlertMapper caishenFundAlertMapper;

    @Override
    public List<FundWatchVO> listMyWatches() {
        Long userId = requireUserId();
        return caishenFundWatchMapper.selectWatchVOs(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "添加关注")
    public Long create(FundWatchCreateDTO dto) {
        Long userId = requireUserId();

        CaishenFundDO fund = caishenFundMapper.selectOne(Wrappers.<CaishenFundDO>lambdaQuery()
                .eq(CaishenFundDO::getFundCode, dto.getFundCode()));
        if (fund == null) {
            throw new BizException(ResultStatus.DATA_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now();
        CaishenFundWatchDO watch = new CaishenFundWatchDO();
        watch.setUserId(userId);
        watch.setFundCode(dto.getFundCode().trim());
        watch.setRemark(dto.getRemark());
        watch.setCreateTime(now);
        watch.setUpdateTime(now);
        // 重复 (user_id, fund_code) 由数据库唯一键兜底，DuplicateKeyException 全局转 9003
        caishenFundWatchMapper.insert(watch);
        return watch.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "取消关注")
    public void delete(Long id) {
        Long userId = requireUserId();
        requireOwnedWatch(id, userId);
        // 显式删除关联提醒规则（FK 级联兜底），与删除关注同事务
        caishenFundAlertMapper.delete(Wrappers.<CaishenFundAlertDO>lambdaQuery()
                .eq(CaishenFundAlertDO::getWatchId, id));
        caishenFundWatchMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "理财中心", action = "更新关注备注")
    public void updateRemark(Long id, FundWatchUpdateDTO dto) {
        Long userId = requireUserId();
        LambdaUpdateWrapper<CaishenFundWatchDO> wrapper = Wrappers.<CaishenFundWatchDO>lambdaUpdate()
                .eq(CaishenFundWatchDO::getId, id)
                .eq(CaishenFundWatchDO::getUserId, userId)
                .set(CaishenFundWatchDO::getRemark, dto.getRemark())
                .set(CaishenFundWatchDO::getUpdateTime, LocalDateTime.now());
        if (caishenFundWatchMapper.update(null, wrapper) == 0) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
    }

    private Long requireUserId() {
        Long userId = CurrentUserContext.getUserId();
        if (userId == null) {
            throw new BizException(ResultStatus.UNAUTHORIZED);
        }
        return userId;
    }

    private CaishenFundWatchDO requireOwnedWatch(Long id, Long userId) {
        CaishenFundWatchDO watch = caishenFundWatchMapper.selectOne(
                Wrappers.<CaishenFundWatchDO>lambdaQuery()
                        .eq(CaishenFundWatchDO::getId, id)
                        .eq(CaishenFundWatchDO::getUserId, userId));
        if (watch == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return watch;
    }
}
