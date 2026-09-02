package xyz.nanian.owl.user.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.user.domain.entity.BlogPostDO;
import xyz.nanian.owl.user.domain.vo.BlogPostVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogPostConvert {

    @Mapping(target = "tags", ignore = true)
    BlogPostVO toVO(BlogPostDO post);

    List<BlogPostVO> toVOList(List<BlogPostDO> posts);
}