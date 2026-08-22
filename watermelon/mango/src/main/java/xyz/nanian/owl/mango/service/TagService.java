package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.mango.domain.dto.TagDTO;
import xyz.nanian.owl.mango.domain.vo.TagVO;

import java.util.List;

/**
 * 博客标签服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface TagService {

    /**
     * 全部标签（含已发布文章数）
     *
     * @return 标签列表
     */
    List<TagVO> list();

    /**
     * 创建标签
     *
     * @param dto 入参
     * @return 新标签 ID
     */
    Long create(TagDTO dto);

    /**
     * 更新标签
     *
     * @param id  标签 ID
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean update(Long id, TagDTO dto);

    /**
     * 删除标签（关联关系级联删除）
     *
     * @param id 标签 ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);
}
