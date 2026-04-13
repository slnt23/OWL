package xyz.nanian.owl.sugarcane.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.sugarcane.domain.dto.ItemDTO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemVO;

/**
 * <p>
 * 被定价物品表 前端控制器
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@RestController
@RequestMapping("/item-do")
@Tag(name = "物品价格查询管理",description = "放置有关物品查询的方法")
public class ItemController {

    /**
     * 查询物品价格信息
     * @param dto
     * @return
     */

    @GetMapping("/item")
    @Operation(summary = "查询物品价格",description = "所查物品名")
    public Result<ItemVO> getItem(ItemDTO dto) {
        return null;
    }
}
