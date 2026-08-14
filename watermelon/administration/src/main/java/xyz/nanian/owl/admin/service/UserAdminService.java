package xyz.nanian.owl.admin.service;

import com.baomidou.mybatisplus.spring.service.IService;
import xyz.nanian.owl.admin.domain.dto.UserCreateDTO;
import xyz.nanian.owl.admin.domain.dto.UserPasswordResetDTO;
import xyz.nanian.owl.admin.domain.dto.UserUpdateDTO;
import xyz.nanian.owl.admin.domain.entity.UserDO;
import xyz.nanian.owl.admin.domain.vo.AdminUserVO;
import xyz.nanian.owl.common.result.ResultPage;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
public interface UserAdminService extends IService<UserDO> {

    ResultPage<AdminUserVO> page(long pageNum, long pageSize, String keyword, Byte status, String roleName);

    AdminUserVO getById(Long id);

    Long create(UserCreateDTO createDTO);

    Boolean update(Long id, UserUpdateDTO updateDTO);

    Boolean deleteById(Long id);

    Boolean updateStatus(Long id, Byte status);

    Boolean updateRole(Long id, String roleName);

    Boolean resetPassword(Long id, UserPasswordResetDTO resetDTO);
}
