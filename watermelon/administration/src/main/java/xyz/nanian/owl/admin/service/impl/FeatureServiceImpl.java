package xyz.nanian.owl.admin.service.impl;

import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.admin.convert.FeatureConvert;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.entity.FeatureDO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;
import xyz.nanian.owl.admin.mapper.FeatureMapper;
import xyz.nanian.owl.admin.service.FeatureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
    public FeatureVO getById(Integer id) {
        FeatureDO featureDO=featureMapper.selectById(id);

        return featureConvert.DOtoVO(featureDO);
    }

    @Override
    public Integer create(FeatureDTO dto) {
        FeatureDO featureDO=featureConvert.DTOtoEntity(dto);

        return featureMapper.insert(featureDO);
    }

    @Override
    public Boolean update(FeatureDTO dto) {
        FeatureDO featureDO = featureConvert.DTOtoEntity(dto);
        int result = featureMapper.updateById(featureDO);

        return result == 1;
    }

    @Override
    public Boolean deleteById(Integer id) {
        int result = featureMapper.deleteById(id);
        return result == 1;
    }
}
