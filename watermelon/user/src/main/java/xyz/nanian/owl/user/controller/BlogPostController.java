package xyz.nanian.owl.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.user.domain.dto.BlogPostCreateDTO;
import xyz.nanian.owl.user.domain.dto.BlogPostUpdateDTO;
import xyz.nanian.owl.user.domain.vo.BlogPostVO;
import xyz.nanian.owl.user.service.BlogPostService;

import java.util.List;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "博客文章")
public class BlogPostController {

    private final BlogPostService blogPostService;

    @GetMapping
    @Operation(summary = "获取我的博客列表")
    public Result<List<BlogPostVO>> list() {
        Long userId = CurrentUserContext.getUserId();
        return Result.success(blogPostService.listByUserId(userId));
    }

    @GetMapping("/page")
    @Operation(summary = "分页获取我的博客列表")
    public Result<ResultPage<BlogPostVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        Long userId = CurrentUserContext.getUserId();
        return Result.success(blogPostService.pageByUserId(userId, pageNum, pageSize));
    }

    @GetMapping("/all/page")
    @Operation(summary = "分页获取全部博客列表（公开）")
    public Result<ResultPage<BlogPostVO>> pageAll(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.success(blogPostService.pageAll(pageNum, pageSize));
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取博客详情")
    public Result<BlogPostVO> getById(@PathVariable Long id) {
        Long userId = CurrentUserContext.getUserId();
        return Result.success(blogPostService.getById(id, userId));
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "创建博客文章")
    public Result<Long> create(@Valid @ModelAttribute BlogPostCreateDTO dto) {
        Long userId = CurrentUserContext.getUserId();
        return Result.success(blogPostService.create(dto, userId));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "更新博客文章")
    public Result<Void> update(@PathVariable Long id, @Valid @ModelAttribute BlogPostUpdateDTO dto) {
        Long userId = CurrentUserContext.getUserId();
        return blogPostService.update(id, dto, userId) ? Result.success() : Result.fail();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除博客文章")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = CurrentUserContext.getUserId();
        return blogPostService.deleteById(id, userId) ? Result.success() : Result.fail();
    }
}