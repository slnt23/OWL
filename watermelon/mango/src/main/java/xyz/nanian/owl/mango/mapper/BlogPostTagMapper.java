package xyz.nanian.owl.mango.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogPostTagDO;

/**
 * 博客文章-标签关联 Mapper
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper
public interface BlogPostTagMapper extends BaseMapper<BlogPostTagDO> {
}
