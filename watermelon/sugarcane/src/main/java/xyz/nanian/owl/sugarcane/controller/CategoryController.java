package xyz.nanian.owl.sugarcane.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.sugarcane.domain.vo.CategoryTreeVO;

import java.util.List;

/**
 * <p>
 * 价格系统分类表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/category")
@Tag(name = "Category分类")
public class CategoryController {

    // 获取分类树（最重要）
    @GetMapping("/tree")
    public List<CategoryTreeVO> getTree(){
        return null;
    }

    // 删除分类
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){};
}

