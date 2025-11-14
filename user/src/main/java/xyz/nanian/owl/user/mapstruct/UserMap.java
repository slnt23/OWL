package xyz.nanian.owl.user.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import xyz.nanian.owl.user.dto.UserRegisterDTO;
import xyz.nanian.owl.user.entity.UserDO;

/**
 * 用户信息的Map
 *
 * @author slnt23
 * @since 2025/11/13
 */

@Mapper
public interface UserMap {

//    实体类，
    UserMap INSTANCE = Mappers.getMapper(UserMap.class);

    /**
     * 由用户注册信息转变为用户DO
     * @param userRegisterDTO 用户注册DTO
     * @return 用户DO
     */
    UserDO registerToUserDO(UserRegisterDTO userRegisterDTO);


}
