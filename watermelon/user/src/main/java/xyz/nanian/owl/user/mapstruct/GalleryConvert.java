package xyz.nanian.owl.user.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.user.domain.dto.GalleryDTO;
import xyz.nanian.owl.user.domain.entity.GalleryDO;
import xyz.nanian.owl.user.domain.vo.GalleryVO;

import java.util.List;

/**
 * Gallery MapStruct 转换器
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Mapper(componentModel = "spring")
public interface GalleryConvert {

    GalleryVO DOConvertVO(GalleryDO galleryDO);

    List<GalleryVO> DOConvertVO(List<GalleryDO> galleryDOList);

    /**
     * id 由数据库自增维护；userId 由 service 从当前登录用户获取；imageUrl/thumbnailUrl 由 service 上传图片后填充；createTime/updateTime 由数据库维护，均忽略映射。
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    GalleryDO DTOConvertDO(GalleryDTO galleryDTO);
}