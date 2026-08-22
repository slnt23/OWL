package xyz.nanian.owl.mango.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogTagDO;

/**
 * 博客标签 Mapper
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper
public interface BlogTagMapper extends BaseMapper<BlogTagDO> {
}
