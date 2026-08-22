package xyz.nanian.owl.admin.convert;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.admin.domain.dto.FeatureDTO;
import xyz.nanian.owl.admin.domain.entity.FeatureDO;
import xyz.nanian.owl.admin.domain.vo.FeatureVO;

import java.util.List;

/**
 * Mapstruct
 *
 * @author slnt23
 * @since 2026/4/25
 */

@Mapper(componentModel = "spring")
public interface FeatureConvert {

    /**
     * createTime/updateTime 由数据库维护，忽略映射。
     */
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    FeatureDO DTOtoEntity(FeatureDTO dto);

    FeatureVO DOtoVO(FeatureDO entity);
    List<FeatureVO> DOtoVO(List<FeatureDO> entity);

}
