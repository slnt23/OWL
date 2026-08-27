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
     * id 由数据库自增维护；createTime/updateTime 由数据库维护，均忽略映射。
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    FeatureDO DTOtoEntity(FeatureDTO dto);

    FeatureVO DOtoVO(FeatureDO entity);
    List<FeatureVO> DOtoVO(List<FeatureDO> entity);

}