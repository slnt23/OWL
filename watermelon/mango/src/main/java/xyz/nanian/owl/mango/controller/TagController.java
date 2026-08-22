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
import xyz.nanian.owl.mango.domain.dto.TagDTO;
import xyz.nanian.owl.mango.domain.vo.TagVO;
import xyz.nanian.owl.mango.service.TagService;

import java.util.List;

/**
 * 博客标签：公开读 + 登录写
 *
 * @author slnt23
 * @since 2026/8/22
 */
@RestController
@RequestMapping("/api/blog/tags")
@RequiredArgsConstructor
@Tag(name = "博客标签")
public class TagController {

    private final TagService tagService;

    /**
     * 全部标签
     */
    @GetMapping
    @Operation(summary = "全部标签")
    public Result<List<TagVO>> list() {
        return Result.success(tagService.list());
    }

    /**
     * 创建标签
     */
    @PostMapping
    @Operation(summary = "创建标签")
    public Result<Long> create(@Valid @RequestBody TagDTO dto) {
        return Result.success(tagService.create(dto));
    }

    /**
     * 更新标签
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新标签")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody TagDTO dto) {
        return tagService.update(id, dto) ? Result.success() : Result.fail();
    }

    /**
     * 删除标签
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除标签")
    public Result<Void> delete(@PathVariable Long id) {
        return tagService.deleteById(id) ? Result.success() : Result.fail();
    }
}
