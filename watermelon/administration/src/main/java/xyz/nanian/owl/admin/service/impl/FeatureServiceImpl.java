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

        return List.of();
    }

    @Override
    public FeatureVO getById(Integer id) {
        FeatureDO featureDO=featureMapper.selectById(id);


        return null;
    }

    @Override
    public FeatureVO create(FeatureDTO dto) {
        FeatureDO featureDO=featureConvert.DTOtoEntity(dto);
        Integer result = featureMapper.insert(featureDO);

        return null;
    }

    @Override
    public FeatureVO update(FeatureDTO dto) {
        FeatureDO featureDO = featureConvert.DTOtoEntity(dto);
        Integer result = featureMapper.updateById(featureDO);

        return null;
    }

    @Override
    public void deleteById(Integer id) {
        featureMapper.deleteById(id);
    }
}
