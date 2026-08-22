package xyz.nanian.owl.mango.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogPostDO;

/**
 * 博客文章 Mapper
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper
public interface BlogPostMapper extends BaseMapper<BlogPostDO> {
}
