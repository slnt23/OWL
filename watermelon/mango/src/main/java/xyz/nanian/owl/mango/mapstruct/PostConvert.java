package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.mango.domain.entity.BlogPostDO;
import xyz.nanian.owl.mango.domain.vo.PostDetailVO;
import xyz.nanian.owl.mango.domain.vo.PostVO;

import java.util.List;

/**
 * 文章实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface PostConvert {

    /**
     * categoryName / tags 由 service 关联查询后填充，此处忽略
     */
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "tags", ignore = true)
    PostVO toVO(BlogPostDO post);

    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "tags", ignore = true)
    PostDetailVO toDetailVO(BlogPostDO post);

    List<PostVO> toVOList(List<BlogPostDO> posts);

    /**
     * 数据库 0/1 整数转为布尔
     */
    default Boolean integerToBoolean(Integer value) {
        return value != null && value == 1;
    }
}
