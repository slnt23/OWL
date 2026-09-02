package xyz.nanian.owl.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.common.result.Result;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.common.security.CurrentUserContext;
import xyz.nanian.owl.user.domain.dto.GalleryDTO;
import xyz.nanian.owl.user.domain.vo.GalleryVO;
import xyz.nanian.owl.user.service.GalleryService;

import java.util.List;

/**
 * <p>
 * 画廊表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-09-02
 */
@RestController
@RequestMapping("/api/gallery")
@RequiredArgsConstructor
@Tag(name = "画廊管理")
public class GalleryController {

    private final GalleryService galleryService;

    /**
     * 获取当前用户的画廊列表，按 sort_order 升序排列
     */
    @GetMapping
    @Operation(summary = "获取我的画廊列表")
    public Result<List<GalleryVO>> list() {
        Long userId = CurrentUserContext.getUserId();
        List<GalleryVO> list = galleryService.listByUserId(userId);
        return Result.success(list);
    }

    /**
     * 分页获取当前用户的画廊列表，按 sort_order 升序排列
     */
    @GetMapping("/page")
    @Operation(summary = "分页获取我的画廊列表")
    public Result<ResultPage<GalleryVO>> page(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        Long userId = CurrentUserContext.getUserId();
        return Result.success(galleryService.pageByUserId(userId, pageNum, pageSize));
    }

    /**
     * 分页获取所有用户的画廊列表
     */
    @GetMapping("/all/page")
    @Operation(summary = "分页获取全部画廊列表")
    public Result<ResultPage<GalleryVO>> pageAll(
            @RequestParam(defaultValue = "1") long pageNum,
            @RequestParam(defaultValue = "10") long pageSize) {
        return Result.success(galleryService.pageAll(pageNum, pageSize));
    }

    /**
     * 根据 id 获取当前用户的单条画廊项
     */
    @Operation(summary = "获取画廊详情")
    @GetMapping("/{id}")
    public Result<GalleryVO> getById(@PathVariable Long id) {
        Long userId = CurrentUserContext.getUserId();
        GalleryVO galleryVO = galleryService.getById(id, userId);
        return Result.success(galleryVO);
    }

    /**
     * 新增画廊项
     */
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "新增画廊项")
    public Result<Integer> create(@Valid @ModelAttribute GalleryDTO dto) {
        Long userId = CurrentUserContext.getUserId();
        int result = galleryService.create(dto, userId);
        return Result.success(result);
    }

    /**
     * 修改画廊项
     */
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Operation(summary = "更新画廊项")
    public Result<Void> update(
            @PathVariable Long id,
            @Valid @ModelAttribute GalleryDTO dto) {
        dto.setId(id);
        Long userId = CurrentUserContext.getUserId();
        Boolean result = galleryService.update(dto, userId);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    /**
     * 删除画廊项
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除画廊项")
    public Result<Void> delete(@PathVariable Long id) {
        Long userId = CurrentUserContext.getUserId();
        Boolean result = galleryService.deleteById(id, userId);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}