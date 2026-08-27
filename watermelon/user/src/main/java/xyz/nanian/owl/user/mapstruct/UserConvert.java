package xyz.nanian.owl.user.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.SendCodeDTO;
// [TO_BE_DELETED] import xyz.nanian.owl.user.domain.dto.UserInfoDTO;
import xyz.nanian.owl.user.domain.entity.UserDO;
import xyz.nanian.owl.user.domain.vo.UserInfoVO;

/**
 * 用户信息的Map
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Mapper(componentModel = "spring")
public interface UserConvert {

    // [TO_BE_DELETED] 旧转换方法，已由 UserInfoUpdateDTO 流程取代。
    // UserDO UserInfoToUserDO(UserInfoDTO userInfoDTO);

    @Mapping(target = "role", ignore = true)
    UserInfoVO UserDOToUserVO(UserDO userDO);
}