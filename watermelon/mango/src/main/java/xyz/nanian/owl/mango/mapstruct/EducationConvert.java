package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import xyz.nanian.owl.mango.domain.entity.BlogEducationDO;
import xyz.nanian.owl.mango.domain.vo.EducationVO;

import java.util.List;

/**
 * 教育经历实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface EducationConvert {

    EducationVO toVO(BlogEducationDO education);

    List<EducationVO> toVOList(List<BlogEducationDO> educationList);
}
