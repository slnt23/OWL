package xyz.nanian.owl.mango.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.mango.domain.dto.PostCreateDTO;
import xyz.nanian.owl.mango.domain.dto.PostQueryDTO;
import xyz.nanian.owl.mango.domain.dto.PostUpdateDTO;
import xyz.nanian.owl.mango.domain.vo.PostDetailVO;
import xyz.nanian.owl.mango.domain.vo.PostVO;
import xyz.nanian.owl.mango.service.PostService;

/**
 * 博客文章：公开读 + 登录写
 *
 * @author slnt23
 * @since 2026/8/22
 */
@RestController
@RequestMapping("/api/blog/posts")
@RequiredArgsConstructor
@Tag(name = "博客文章")
public class PostController {

    private final PostService postService;

    /**
     * 文章分页列表，支持标签/分类/关键词/语言筛选
     */
    @GetMapping
    @Operation(summary = "文章分页列表")
    public Result<ResultPage<PostVO>> page(PostQueryDTO query) {
        return Result.success(postService.page(query));
    }

    /**
     * 按 ID 获取文章详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "文章详情")
    public Result<PostDetailVO> getById(@PathVariable Long id) {
        return Result.success(postService.getById(id));
    }

    /**
     * 按 slug 获取文章详情
     */
    @GetMapping("/slug/{slug}")
    @Operation(summary = "按 slug 获取文章详情")
    public Result<PostDetailVO> getBySlug(@PathVariable String slug) {
        return Result.success(postService.getBySlug(slug));
    }

    /**
     * 创建文章
     */
    @PostMapping
    @Operation(summary = "创建文章")
    public Result<Long> create(@Valid @RequestBody PostCreateDTO dto) {
        return Result.success(postService.create(dto));
    }

    /**
     * 更新文章
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新文章")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO dto) {
        return postService.update(id, dto) ? Result.success() : Result.fail();
    }

    /**
     * 删除文章
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除文章")
    public Result<Void> delete(@PathVariable Long id) {
        return postService.deleteById(id) ? Result.success() : Result.fail();
    }
}
