package xyz.nanian.owl.sugarcane.controller;

import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
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
public class CategoryController {

    // 获取分类树（最重要）
    @GetMapping("/tree")
    public List<CategoryTreeVO> getTree(){
        return null;
    }

    // 新增分类
//    @PostMapping
//    public void create(@RequestBody CategoryCreateDTO dto){};

    // 更新分类
//    @PutMapping("/{id}")
//    public void update(@PathVariable Long id,
//                       @RequestBody CategoryUpdateDTO dto){};

    // 删除分类
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){};

    // 获取单个分类
    @GetMapping("/{id}")
    public CategoryVO getById(@PathVariable Long id){
        return null;
    }
}

