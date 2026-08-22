package xyz.nanian.owl.mango.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogCategoryDO;

/**
 * 博客分类 Mapper
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper
public interface BlogCategoryMapper extends BaseMapper<BlogCategoryDO> {
}
