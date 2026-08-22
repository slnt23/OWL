package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.mango.domain.entity.BlogCategoryDO;
import xyz.nanian.owl.mango.domain.vo.CategoryVO;

import java.util.List;

/**
 * 分类实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface CategoryConvert {

    /**
     * postCount 由 service 统计后填充，此处忽略
     */
    @Mapping(target = "postCount", ignore = true)
    CategoryVO toVO(BlogCategoryDO category);

    List<CategoryVO> toVOList(List<BlogCategoryDO> categories);
}
