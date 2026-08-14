package xyz.nanian.owl.admin.convert;


import org.mapstruct.Mapper;
import xyz.nanian.owl.admin.domain.entity.UserDO;
import xyz.nanian.owl.admin.domain.vo.AdminUserVO;

import java.util.List;

/**
 * 后台用户管理 MapStruct
 *
 * @author slnt23
 * @since 2026/8/14
 */
@Mapper(componentModel = "spring")
public interface UserAdminConvert {

    AdminUserVO toVO(UserDO userDO);

    List<AdminUserVO> toVO(List<UserDO> userDOS);
}
