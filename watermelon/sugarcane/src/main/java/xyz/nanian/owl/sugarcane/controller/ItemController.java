package xyz.nanian.owl.sugarcane.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.result.ResultPage;
import xyz.nanian.owl.sugarcane.domain.dto.ItemIntroDTO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemIntroVO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemVO;
import xyz.nanian.owl.sugarcane.service.ItemService;

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
@Tag(name = "被定价物品管理")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

//     分页查询
    @PostMapping("/page")
    @Operation(summary = "所查物品列表")
    public ResultPage<ItemIntroVO> page(@RequestBody ItemIntroDTO dto){

        return ResultPage.create(itemService.getItemIntroList(dto));
    }

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

