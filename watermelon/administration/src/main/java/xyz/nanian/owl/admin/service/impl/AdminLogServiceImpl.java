package xyz.nanian.owl.admin.service.impl;

import xyz.nanian.owl.admin.domain.entity.AdminLogDO;
import xyz.nanian.owl.admin.mapper.AdminLogMapper;
import xyz.nanian.owl.admin.service.AdminLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员操作日志表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLogDO> implements AdminLogService {

}
