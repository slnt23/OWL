package xyz.nanian.owl.admin.convert;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.entity.SpotlightDO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;

import java.util.List;

/**
 * Mapstruct
 *
 * @author slnt23
 * @since 2026/4/25
 */

@Mapper(componentModel = "spring")
public interface SpotlightConvert {
    SpotlightVO DOConvertVO(SpotlightDO spotlightDO);
    List<SpotlightVO> DOConvertVO(List<SpotlightDO> spotlightDOS);

    /**
     * imageUrl 由 service 上传图片后填充；createTime/updateTime 由数据库维护，均忽略映射。
     */
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    SpotlightDO DTOConvertDO(SpotlightDTO spotlightDTO);
}
