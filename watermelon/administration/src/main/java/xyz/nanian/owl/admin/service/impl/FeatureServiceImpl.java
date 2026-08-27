package xyz.nanian.owl.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.admin.convert.FeatureConvert;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.entity.FeatureDO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;
import xyz.nanian.owl.admin.mapper.FeatureMapper;
import xyz.nanian.owl.admin.service.FeatureService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;

import java.util.List;

/**
 * <p>
 * 产品特性展示表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@Service
@RequiredArgsConstructor
public class FeatureServiceImpl extends ServiceImpl<FeatureMapper, FeatureDO> implements FeatureService {

    private final FeatureMapper featureMapper;
    private final FeatureConvert featureConvert;

    @Override
    public List<FeatureVO> listByOrder() {
        List<FeatureDO> doList= featureMapper.selectLists();
        return featureConvert.DOtoVO(doList);
    }

    @Override
    public ResultPage<FeatureVO> page(long pageNum, long pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        Page<FeatureDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<FeatureDO> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(FeatureDO::getSortOrder);

        IPage<FeatureDO> result = featureMapper.selectPage(page, wrapper);
        List<FeatureVO> records = featureConvert.DOtoVO(result.getRecords());

        ResultPage<FeatureVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public FeatureVO getById(Long id) {
        FeatureDO featureDO=featureMapper.selectById(id);

        return featureConvert.DOtoVO(featureDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "新增产品特性", persist = true)
    public Integer create(FeatureDTO dto) {
        FeatureDO featureDO=featureConvert.DTOtoEntity(dto);
        return featureMapper.insert(featureDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "修改产品特性", persist = true)
    public Boolean update(FeatureDTO dto) {
        FeatureDO featureDO = featureConvert.DTOtoEntity(dto);
        featureDO.setId(dto.getId());
        int result = featureMapper.updateById(featureDO);

        return result == 1;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "删除产品特性", persist = true)
    public Boolean deleteById(Long id) {
        int result = featureMapper.deleteById(id);
        return result == 1;
    }
}