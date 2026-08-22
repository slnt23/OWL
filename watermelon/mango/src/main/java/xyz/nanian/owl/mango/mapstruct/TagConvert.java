package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.mango.domain.entity.BlogTagDO;
import xyz.nanian.owl.mango.domain.vo.TagVO;

import java.util.List;

/**
 * 标签实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface TagConvert {

    /**
     * postCount 由 service 统计后填充，此处忽略
     */
    @Mapping(target = "postCount", ignore = true)
    TagVO toVO(BlogTagDO tag);

    List<TagVO> toVOList(List<BlogTagDO> tags);
}
