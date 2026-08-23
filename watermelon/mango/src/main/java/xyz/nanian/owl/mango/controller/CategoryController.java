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
import xyz.nanian.owl.mango.domain.dto.CategoryDTO;
import xyz.nanian.owl.mango.domain.vo.CategoryVO;
import xyz.nanian.owl.mango.service.CategoryService;

import java.util.List;

/**
 * 博客分类：公开读 + 登录写
 *
 * @author slnt23
 * @since 2026/8/22
 */
@RestController("mangoCategoryController")
@RequestMapping("/api/blog/categories")
@RequiredArgsConstructor
@Tag(name = "博客分类")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 全部分类
     */
    @GetMapping
    @Operation(summary = "全部分类")
    public Result<List<CategoryVO>> list() {
        return Result.success(categoryService.list());
    }

    /**
     * 创建分类
     */
    @PostMapping
    @Operation(summary = "创建分类")
    public Result<Long> create(@Valid @RequestBody CategoryDTO dto) {
        return Result.success(categoryService.create(dto));
    }

    /**
     * 更新分类
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新分类")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        return categoryService.update(id, dto) ? Result.success() : Result.fail();
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public Result<Void> delete(@PathVariable Long id) {
        return categoryService.deleteById(id) ? Result.success() : Result.fail();
    }
}
