package xyz.nanian.owl.user.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.user.domain.entity.BlogSettingsDO;
import xyz.nanian.owl.user.domain.vo.BlogAboutVO;

@Mapper(componentModel = "spring")
public interface BlogSettingsConvert {

    @Mapping(target = "bio", ignore = true)
    BlogAboutVO toAboutVO(BlogSettingsDO settings);
}