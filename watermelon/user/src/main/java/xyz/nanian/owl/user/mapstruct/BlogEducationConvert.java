package xyz.nanian.owl.user.mapstruct;

import org.mapstruct.Mapper;
import xyz.nanian.owl.user.domain.entity.BlogEducationDO;
import xyz.nanian.owl.user.domain.vo.BlogEducationVO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BlogEducationConvert {

    BlogEducationVO toVO(BlogEducationDO education);

    List<BlogEducationVO> toVOList(List<BlogEducationDO> educations);
}