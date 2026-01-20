package xyz.nanian.owl.user.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import xyz.nanian.owl.user.dto.UserInfoDTO;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;

/**
 * 用户信息的Map
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Mapper(componentModel = "spring")
public interface UserConvert {

//    实体类，
//    UserConvert INSTANCE = Mappers.getMapper(UserConvert.class);

    /**
     * 由用户注册信息转变为用户DO
     * @param userRegisterDTO 用户注册DTO
     * @return 用户DO
     */
    UserDO registerDTOToUserDO(UserRegisterDTO userRegisterDTO);

    /**
     * 对于MapStruct不能将 ，例如DO复制到另一个有一些信息的DO，只能新创造一个DO，并复制数据，
     * 更改用户信息，
     * @param userInfoDTO 用户自定义信息
     * @return 用户信息
     */
    UserDO UserInfoToUserDO(UserInfoDTO userInfoDTO);

//    /**
//     * 将新信息粘贴
//     * @param userDO 最新用户信息
//     * @return 用户信息DO
//     */
//    UserDO updateUserDO(UserDO userDO);

    @Mapping(source = "id",target = "userId")
    UserInfoDTO userDOToUserInfoDTO(UserDO userDO);
}
