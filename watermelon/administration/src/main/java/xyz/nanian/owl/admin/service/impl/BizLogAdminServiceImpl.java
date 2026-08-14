package xyz.nanian.owl.admin.service.impl;

import xyz.nanian.owl.log.domain.entity.BizLogDO;
import xyz.nanian.owl.admin.mapper.BizLogAdminMapper;
import xyz.nanian.owl.admin.service.BizLogAdminService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务操作日志表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-13 23:53:18
 */
@Service
public class BizLogAdminServiceImpl extends ServiceImpl<BizLogAdminMapper, BizLogDO> implements BizLogAdminService {

}
