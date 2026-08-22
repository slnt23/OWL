package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogProfileDO;
import xyz.nanian.owl.mango.domain.vo.ProfileVO;

/**
 * 站长个人信息实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface ProfileConvert {

    ProfileVO toVO(BlogProfileDO profile);
}
