package xyz.nanian.owl.pitaya.consumer.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.result.ResultStatus;
import xyz.nanian.owl.pitaya.consumer.service.ConCartService;
import xyz.nanian.owl.pitaya.domain.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.domain.vo.ShoppingCartVO;
import xyz.nanian.owl.result.ResultPage;
import xyz.nanian.owl.result.Result;

/**
 * 消费者购物车Controller
 *
 * @author slnt23
 * @since 2026/1/17
 */

@RestController
@RequestMapping("/pitaya/cart/consumer")
@Tag(name = "消费者购物车管理",description = "购物车相关")
@RequiredArgsConstructor
public class ConCartController {

    private final ConCartService conCartService;

//    使用lombok 的@RequiredArgsConstructor 会自动对final 字段创建构造函数
//    public ConCartController(ConCartService conCartService) {
//        this.conCartService = conCartService;
//    }

    /**
     * 添加购物车中商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/product")
    @Operation(summary = "新增购物车商品",description = "传DTO,添加商品")
    public Result<ResultStatus> addProduct(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        if(conCartService.saveProduct(shoppingCartDTO)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    /**
     * 删除购物车中商品
     * @param userId
     * @param productId
     * @return
     */
    @DeleteMapping("/product")
    @Operation(summary = "删除购物车商品",description = "通过用户ID，商品ID")
    public Result<ResultStatus> removeProduct(
            @RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {

        if(conCartService.deleteProduct(userId,productId)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    /**
     * 更新购物车中商品
     * @param shoppingCartDTO
     * @return
     */
    @PutMapping("/product")
    @Operation(summary = "更新购物车商品",description = "通过DTO")
    public Result<ResultStatus> updateProduct(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        if(conCartService.updateProduct(shoppingCartDTO)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    /**
     * 查询购物车商品列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Operation(summary = "购物车列表")
    @GetMapping("/carts")
    public Result<ResultPage<ShoppingCartVO>> queryCartList(@RequestParam Integer pageNum,
                                                            @RequestParam Integer pageSize) {

        return Result.success(conCartService.listCart(pageNum,pageSize));
    }
}
