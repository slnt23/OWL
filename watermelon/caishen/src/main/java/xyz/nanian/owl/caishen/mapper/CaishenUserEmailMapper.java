package xyz.nanian.owl.caishen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.caishen.domain.entity.CaishenUserEmailDO;

/**
 * user 表最小映射（id + email），供定时任务批量取收件邮箱。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Mapper
public interface CaishenUserEmailMapper extends BaseMapper<CaishenUserEmailDO> {
}
