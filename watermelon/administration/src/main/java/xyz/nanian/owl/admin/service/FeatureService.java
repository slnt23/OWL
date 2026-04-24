package xyz.nanian.owl.admin.service;

import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.entity.FeatureDO;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;

import java.util.List;

/**
 * <p>
 * 产品特性展示表 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
public interface FeatureService extends IService<FeatureDO> {

    List<FeatureVO> listByOrder();
    FeatureVO getById(Integer id);
    FeatureVO create(FeatureDTO dto);
    FeatureVO update(FeatureDTO vo);
    void deleteById(Integer id);
}
