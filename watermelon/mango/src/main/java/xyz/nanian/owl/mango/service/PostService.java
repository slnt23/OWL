package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.mango.domain.dto.PostCreateDTO;
import xyz.nanian.owl.mango.domain.dto.PostQueryDTO;
import xyz.nanian.owl.mango.domain.dto.PostUpdateDTO;
import xyz.nanian.owl.mango.domain.vo.PostDetailVO;
import xyz.nanian.owl.mango.domain.vo.PostVO;

/**
 * 博客文章服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface PostService {

    /**
     * 文章分页列表
     *
     * @param query 分页与筛选参数
     * @return 分页结果
     */
    ResultPage<PostVO> page(PostQueryDTO query);

    /**
     * 按 ID 获取文章详情，浏览量 +1
     *
     * @param id 文章 ID
     * @return 详情
     */
    PostDetailVO getById(Long id);

    /**
     * 按 slug 获取文章详情，浏览量 +1
     *
     * @param slug URL 标识
     * @return 详情
     */
    PostDetailVO getBySlug(String slug);

    /**
     * 创建文章
     *
     * @param dto 创建入参
     * @return 新文章 ID
     */
    Long create(PostCreateDTO dto);

    /**
     * 更新文章
     *
     * @param id  文章 ID
     * @param dto 更新入参
     * @return 是否更新成功
     */
    Boolean update(Long id, PostUpdateDTO dto);

    /**
     * 删除文章
     *
     * @param id 文章 ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);
}
