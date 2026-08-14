package xyz.nanian.owl.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.admin.domain.dto.RoleCreateDTO;
import xyz.nanian.owl.admin.domain.dto.RoleUpdateDTO;
import xyz.nanian.owl.admin.domain.entity.UserDO;
import xyz.nanian.owl.admin.domain.vo.RoleVO;
import xyz.nanian.owl.admin.mapper.RoleAdminMapper;
import xyz.nanian.owl.admin.mapper.UserAdminMapper;
import xyz.nanian.owl.admin.service.RoleAdminService;
import xyz.nanian.owl.api.domain.entity.RoleDO;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Service
@RequiredArgsConstructor
public class RoleAdminServiceImpl extends ServiceImpl<RoleAdminMapper, RoleDO> implements RoleAdminService {

    private final RoleAdminMapper roleAdminMapper;
    private final UserAdminMapper userAdminMapper;

    @Override
    public List<RoleVO> listAll() {
        List<RoleDO> roleDOS = roleAdminMapper.selectList(
                Wrappers.<RoleDO>lambdaQuery().orderByAsc(RoleDO::getId));
        return roleDOS.stream().map(this::toVO).toList();
    }

    @Override
    public RoleVO getById(Long id) {
        return toVO(requireRole(id));
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "角色管理", action = "新增角色", persist = true)
    public Long create(RoleCreateDTO createDTO) {
        String roleName = createDTO.getRoleName().trim();
        if (existsByRoleName(roleName, null)) {
            throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
        }

        RoleDO roleDO = new RoleDO();
        roleDO.setRoleName(roleName);
        roleDO.setDescription(createDTO.getDescription());
        roleDO.setEnabled(createDTO.getEnabled() == null || createDTO.getEnabled());
        roleDO.setCreateTime(LocalDateTime.now());
        roleDO.setUpdateTime(LocalDateTime.now());
        roleAdminMapper.insert(roleDO);
        return roleDO.getId();
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "角色管理", action = "更新角色", persist = true)
    public Boolean update(Long id, RoleUpdateDTO updateDTO) {
        requireRole(id);

        LambdaUpdateWrapper<RoleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleDO::getId, id);
        boolean changed = false;

        if (updateDTO.getRoleName() != null && !updateDTO.getRoleName().isBlank()) {
            String roleName = updateDTO.getRoleName().trim();
            if (existsByRoleName(roleName, id)) {
                throw new BizException(ResultStatus.DATA_ALREADY_EXIST);
            }
            wrapper.set(RoleDO::getRoleName, roleName);
            changed = true;
        }
        if (updateDTO.getDescription() != null) {
            wrapper.set(RoleDO::getDescription, updateDTO.getDescription().trim());
            changed = true;
        }
        if (updateDTO.getEnabled() != null) {
            checkRoleCanDisable(id, updateDTO.getEnabled());
            wrapper.set(RoleDO::getEnabled, updateDTO.getEnabled());
            changed = true;
        }

        return !changed || roleAdminMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "角色管理", action = "删除角色", persist = true)
    public Boolean deleteById(Long id) {
        requireRole(id);
        RoleDO roleDO = requireRole(id);
        if (isRoleInUse(roleDO.getRoleName())) {
            throw new BizException(ResultStatus.ROLE_IN_USE);
        }
        return roleAdminMapper.deleteById(id) > 0;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "角色管理", action = "启用/禁用角色", persist = true)
    public Boolean updateEnabled(Long id, Boolean enabled) {
        requireRole(id);
        checkRoleCanDisable(id, enabled);

        LambdaUpdateWrapper<RoleDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(RoleDO::getId, id).set(RoleDO::getEnabled, enabled);
        return roleAdminMapper.update(null, wrapper) > 0;
    }

    private RoleDO requireRole(Long id) {
        RoleDO roleDO = roleAdminMapper.selectById(id);
        if (roleDO == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return roleDO;
    }

    private RoleVO toVO(RoleDO roleDO) {
        RoleVO roleVO = new RoleVO();
        roleVO.setId(roleDO.getId());
        roleVO.setRoleName(roleDO.getRoleName());
        roleVO.setDescription(roleDO.getDescription());
        roleVO.setEnabled(roleDO.getEnabled());
        roleVO.setCreateTime(roleDO.getCreateTime());
        roleVO.setUpdateTime(roleDO.getUpdateTime());
        return roleVO;
    }

    private boolean existsByRoleName(String roleName, Long excludeId) {
        LambdaQueryWrapper<RoleDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(RoleDO::getRoleName, roleName);
        if (excludeId != null) {
            wrapper.ne(RoleDO::getId, excludeId);
        }
        return roleAdminMapper.selectCount(wrapper) > 0;
    }

    private boolean isRoleInUse(String roleName) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(UserDO::getRoleName, roleName);
        return userAdminMapper.selectCount(wrapper) > 0;
    }

    private void checkRoleCanDisable(Long id, Boolean enabled) {
        RoleDO roleDO = requireRole(id);
        if (Boolean.FALSE.equals(enabled) && isRoleInUse(roleDO.getRoleName())) {
            throw new BizException(ResultStatus.ROLE_IN_USE);
        }
    }

}
