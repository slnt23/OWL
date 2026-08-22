package xyz.nanian.owl.mango.mapstruct;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.mango.domain.entity.BlogSkillCategoryDO;
import xyz.nanian.owl.mango.domain.entity.BlogSkillItemDO;
import xyz.nanian.owl.mango.domain.vo.SkillCategoryVO;
import xyz.nanian.owl.mango.domain.vo.SkillItemVO;

import java.util.List;

/**
 * 技能实体与 VO 转换
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Mapper(componentModel = "spring")
public interface SkillConvert {

    SkillItemVO itemToVO(BlogSkillItemDO item);

    List<SkillItemVO> itemListToVOList(List<BlogSkillItemDO> items);

    /**
     * items 由 service 关联查询后填充，此处忽略
     */
    @Mapping(target = "items", ignore = true)
    SkillCategoryVO categoryToVO(BlogSkillCategoryDO category);
}
