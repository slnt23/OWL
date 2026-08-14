package xyz.nanian.owl.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.admin.convert.UserAdminConvert;
import xyz.nanian.owl.admin.domain.dto.UserCreateDTO;
import xyz.nanian.owl.admin.domain.dto.UserPasswordResetDTO;
import xyz.nanian.owl.admin.domain.dto.UserUpdateDTO;
import xyz.nanian.owl.admin.domain.entity.UserDO;
import xyz.nanian.owl.admin.domain.vo.AdminUserVO;
import xyz.nanian.owl.admin.mapper.UserAdminMapper;
import xyz.nanian.owl.admin.service.UserAdminService;
import xyz.nanian.owl.api.domain.entity.RoleDO;
import xyz.nanian.owl.api.mapper.RoleMapper;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.common.security.TokenRevocationService;
import xyz.nanian.owl.common.utils.regex.RegexUtil;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Service
@RequiredArgsConstructor
public class UserAdminServiceImpl extends ServiceImpl<UserAdminMapper, UserDO> implements UserAdminService {

    private final UserAdminMapper userAdminMapper;
    private final RoleMapper roleMapper;
    private final UserAdminConvert userAdminConvert;
    private final PasswordEncoder passwordEncoder;
    private final TokenRevocationService tokenRevocationService;

    @Override
    public ResultPage<AdminUserVO> page(long pageNum, long pageSize, String keyword, Byte status, Long roleId) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        Page<UserDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        if (keyword != null && !keyword.isBlank()) {
            String like = keyword.trim();
            wrapper.and(w -> w.like(UserDO::getUsername, like)
                    .or().like(UserDO::getNickname, like)
                    .or().like(UserDO::getPhone, like)
                    .or().like(UserDO::getEmail, like));
        }
        if (status != null) {
            wrapper.eq(UserDO::getStatus, status);
        }
        if (roleId != null) {
            wrapper.eq(UserDO::getRoleId, roleId);
        }
        wrapper.orderByDesc(UserDO::getCreateTime);

        IPage<UserDO> result = userAdminMapper.selectPage(page, wrapper);
        List<AdminUserVO> records = userAdminConvert.toVO(result.getRecords());
        records.forEach(this::fillRoleName);

        ResultPage<AdminUserVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(records);
        return pageResult;
    }

    @Override
    public AdminUserVO getById(Long id) {
        UserDO userDO = requireUser(id);
        AdminUserVO userVO = userAdminConvert.toVO(userDO);
        fillRoleName(userVO);
        return userVO;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "新增用户", persist = true)
    public Long create(UserCreateDTO createDTO) {
        String username = createDTO.getUsername().trim();
        String email = createDTO.getEmail().trim();
        validatePassword(createDTO.getPassword());
        validateEmail(email);
        validatePhone(createDTO.getPhone());

        if (existsByUsername(username, null)) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }
        if (existsByEmail(email, null)) {
            throw new BizException(ResultStatus.EMAIL_ALREADY_BOUND);
        }
        String phone = normalizePhone(createDTO.getPhone());
        if (phone != null && existsByPhone(phone, null)) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }
        requireEnabledRole(createDTO.getRoleId());
        validateStatus(createDTO.getStatus());

        UserDO userDO = new UserDO();
        userDO.setUserCode(UUID.randomUUID().toString());
        userDO.setUsername(username);
        userDO.setPassword(passwordEncoder.encode(createDTO.getPassword()));
        userDO.setEmail(email);
        userDO.setPhone(phone);
        userDO.setNickname(createDTO.getNickname() == null || createDTO.getNickname().isBlank()
                ? username
                : createDTO.getNickname().trim());
        userDO.setRoleId(createDTO.getRoleId());
        userDO.setStatus(createDTO.getStatus() == null ? 0 : createDTO.getStatus());
        userDO.setRemark(createDTO.getRemark());
        userDO.setCreateTime(LocalDateTime.now());
        userDO.setUpdateTime(LocalDateTime.now());

        userAdminMapper.insert(userDO);
        return userDO.getId();
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "更新用户", persist = true)
    public Boolean update(Long id, UserUpdateDTO updateDTO) {
        requireUser(id);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, id);
        boolean changed = false;

        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isBlank()) {
            String username = updateDTO.getUsername().trim();
            if (existsByUsername(username, id)) {
                throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
            }
            wrapper.set(UserDO::getUsername, username);
            changed = true;
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
            String email = updateDTO.getEmail().trim();
            validateEmail(email);
            if (existsByEmail(email, id)) {
                throw new BizException(ResultStatus.EMAIL_ALREADY_BOUND);
            }
            wrapper.set(UserDO::getEmail, email);
            changed = true;
        }
        if (updateDTO.getPhone() != null) {
            String phone = normalizePhone(updateDTO.getPhone());
            if (phone != null && existsByPhone(phone, id)) {
                throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
            }
            wrapper.set(UserDO::getPhone, phone);
            changed = true;
        }
        if (updateDTO.getNickname() != null) {
            wrapper.set(UserDO::getNickname, updateDTO.getNickname().trim());
            changed = true;
        }
        if (updateDTO.getRemark() != null) {
            wrapper.set(UserDO::getRemark, updateDTO.getRemark().trim());
            changed = true;
        }
        if (updateDTO.getRoleId() != null) {
            requireEnabledRole(updateDTO.getRoleId());
            wrapper.set(UserDO::getRoleId, updateDTO.getRoleId());
            changed = true;
        }
        if (updateDTO.getStatus() != null) {
            validateStatus(updateDTO.getStatus());
            wrapper.set(UserDO::getStatus, updateDTO.getStatus());
            changed = true;
        }

        return !changed || userAdminMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "删除用户", persist = true)
    public Boolean deleteById(Long id) {
        requireUser(id);
        return userAdminMapper.deleteById(id) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "封禁/解封用户", persist = true)
    public Boolean updateStatus(Long id, Byte status) {
        requireUser(id);
        validateStatus(status);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, id).set(UserDO::getStatus, status);
        return userAdminMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "修改用户角色", persist = true)
    public Boolean updateRole(Long id, Long roleId) {
        requireUser(id);
        requireEnabledRole(roleId);

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, id).set(UserDO::getRoleId, roleId);
        return userAdminMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "用户管理", action = "重置用户密码", persist = true)
    public Boolean resetPassword(Long id, UserPasswordResetDTO resetDTO) {
        requireUser(id);
        validatePassword(resetDTO.getNewPassword());

        LambdaUpdateWrapper<UserDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(UserDO::getId, id)
                .set(UserDO::getPassword, passwordEncoder.encode(resetDTO.getNewPassword()));
        boolean updated = userAdminMapper.update(null, wrapper) > 0;
        if (updated) {
            tokenRevocationService.bumpVersion(id);
        }
        return updated;
    }

    private UserDO requireUser(Long id) {
        UserDO userDO = userAdminMapper.selectById(id);
        if (userDO == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return userDO;
    }

    private void fillRoleName(AdminUserVO userVO) {
        if (userVO.getRoleId() == null) {
            return;
        }
        RoleDO roleDO = roleMapper.selectById(userVO.getRoleId());
        userVO.setRoleName(roleDO == null ? null : roleDO.getRoleName());
    }

    private RoleDO requireEnabledRole(Long roleId) {
        RoleDO roleDO = roleMapper.selectById(roleId);
        if (roleDO == null || !Boolean.TRUE.equals(roleDO.getEnabled())) {
            throw new BizException(ResultStatus.ROLE_FAILED);
        }
        return roleDO;
    }

    private boolean existsByUsername(String username, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getUsername, username);
        if (excludeId != null) {
            wrapper.ne(UserDO::getId, excludeId);
        }
        return userAdminMapper.selectCount(wrapper) > 0;
    }

    private boolean existsByEmail(String email, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getEmail, email);
        if (excludeId != null) {
            wrapper.ne(UserDO::getId, excludeId);
        }
        return userAdminMapper.selectCount(wrapper) > 0;
    }

    private boolean existsByPhone(String phone, Long excludeId) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getPhone, phone);
        if (excludeId != null) {
            wrapper.ne(UserDO::getId, excludeId);
        }
        return userAdminMapper.selectCount(wrapper) > 0;
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank() || !RegexUtil.isEmail(email)) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
    }

    private void validatePhone(String phone) {
        if (phone != null && !phone.isBlank() && !RegexUtil.isPhone(phone)) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
    }

    private String normalizePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return null;
        }
        return phone.trim();
    }

    private void validateStatus(Byte status) {
        if (status != null && status != 0 && status != 1) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
    }

    private void validatePassword(String password) {
        if (password == null
                || password.length() < 8
                || password.length() > 64
                || !password.chars().anyMatch(Character::isLetter)
                || !password.chars().anyMatch(Character::isDigit)) {
            throw new BizException(ResultStatus.PARAMS_INVALID);
        }
    }

}
