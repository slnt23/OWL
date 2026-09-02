// [MOVED_TO_USER_MODULE] 博客文章服务接口已迁移至 user 模块 BlogPostService
// package xyz.nanian.owl.mango.service;
//
// import xyz.nanian.owl.common.result.ResultPage;
// import xyz.nanian.owl.mango.domain.dto.PostCreateDTO;
// import xyz.nanian.owl.mango.domain.dto.PostQueryDTO;
// import xyz.nanian.owl.mango.domain.dto.PostUpdateDTO;
// import xyz.nanian.owl.mango.domain.vo.PostDetailVO;
// import xyz.nanian.owl.mango.domain.vo.PostVO;
//
// public interface PostService {
//     ResultPage<PostVO> page(PostQueryDTO query);
//     PostDetailVO getById(Long id);
//     PostDetailVO getBySlug(String slug);
//     Long create(PostCreateDTO dto);
//     Boolean update(Long id, PostUpdateDTO dto);
//     Boolean deleteById(Long id);
// }