package xyz.nanian.owl.user.service;

import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.user.domain.dto.BlogPostCreateDTO;
import xyz.nanian.owl.user.domain.dto.BlogPostUpdateDTO;
import xyz.nanian.owl.user.domain.vo.BlogPostVO;

import java.util.List;

public interface BlogPostService {

    List<BlogPostVO> listByUserId(Long userId);

    ResultPage<BlogPostVO> pageByUserId(Long userId, long pageNum, long pageSize);

    ResultPage<BlogPostVO> pageAll(long pageNum, long pageSize);

    BlogPostVO getById(Long id, Long userId);

    Long create(BlogPostCreateDTO dto, Long userId);

    Boolean update(Long id, BlogPostUpdateDTO dto, Long userId);

    Boolean deleteById(Long id, Long userId);
}