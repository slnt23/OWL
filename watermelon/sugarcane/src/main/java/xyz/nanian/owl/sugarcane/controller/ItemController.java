package xyz.nanian.owl.sugarcane.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.sugarcane.domain.dto.ItemDTO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemVO;

import java.util.List;

/**
 * <p>
 * 被定价物品表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    // 分页查询
//    @PostMapping("/page")
//    public PageResult<ItemVO> page(@RequestBody ItemQueryDTO dto){
//        return null;
//    }

    // 创建
//    @PostMapping
//    public void create(@RequestBody ItemCreateDTO dto){
//
//    }

    // 更新
//    @PutMapping("/{id}")
//    public void update(@PathVariable Long id,
//                       @RequestBody ItemUpdateDTO dto){
//
//    }

    // 删除
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    }

    // 详情
    @GetMapping("/{id}")
    public ItemVO detail(@PathVariable Long id){
        return null;
    }


    @GetMapping("/search")
    public List<ItemVO> search(@RequestParam String keyword){
        return null;
    }
}

