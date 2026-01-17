package xyz.nanian.owl.pitaya.consumer.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.constant.ResultStatus;
import xyz.nanian.owl.pitaya.consumer.cart.CartApi;
import xyz.nanian.owl.pitaya.consumer.service.ConCartService;
import xyz.nanian.owl.pitaya.dto.ShoppingCartDTO;
import xyz.nanian.owl.pitaya.vo.ShoppingCartVO;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 消费者购物车Controller
 *
 * @author slnt23
 * @since 2026/1/17
 */

@RestController
@RequestMapping("/pitaya/cart/consumer")
@Tag(name = "消费者购物车管理接口",description = "购物车相关")
public class ConCartController implements CartApi {

    private ConCartService conCartService;

    public ConCartController(ConCartService conCartService) {
        this.conCartService = conCartService;
    }

    @Override
    @PostMapping("/product")
    @Operation(summary = "添加购物车中商品",description = "传DTO,添加商品")
    public Result<ResultStatus> addProduct(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        if(conCartService.saveProduct(shoppingCartDTO)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    @Override
    @DeleteMapping("/product")
    @Operation(summary = "删除购物车中商品",description = "通过用户ID，商品ID")
    public Result<ResultStatus> removeProduct(
            @RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {

        if(conCartService.deleteProduct(userId,productId)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    @Override
    @PutMapping("/product")
    @Operation(summary = "更新购物车中商品",description = "通过DTO")
    public Result<ResultStatus> updateProduct(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        if(conCartService.updateProduct(shoppingCartDTO)){
            return Result.success();
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result<List<ShoppingCartVO>> queryCartList(Long userId) {
        return null;
    }
}
