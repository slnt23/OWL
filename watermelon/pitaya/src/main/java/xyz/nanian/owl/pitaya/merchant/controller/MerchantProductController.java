package xyz.nanian.owl.pitaya.merchant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.pitaya.domain.dto.ProductDTO;
import xyz.nanian.owl.pitaya.merchant.service.MerchantProductService;
import xyz.nanian.owl.result.Result;
import xyz.nanian.owl.result.ResultStatus;

/**
 * 商家商品管理接口
 *
 * @author slnt23
 * @since 2026/1/15
 */

@RestController
@RequestMapping("/pitaya/product/merchant")
@Tag(name = "商家商品管理",description = "商家商品")
public class MerchantProductController {

    MerchantProductService merchantProductService;

    public MerchantProductController(MerchantProductService merchantProductService) {
        this.merchantProductService = merchantProductService;
    }

    /**
     * 新增商品
     * @param productDTO DTO
     * @return
     */
    @PostMapping("/products")
    @Operation(summary = "新增商品")
    public Result<ResultStatus> addProduct(@RequestBody @Validated ProductDTO productDTO) {

        if(merchantProductService.saveProduct(productDTO)) {
            return Result.success(ResultStatus.CREATED);
        }else {
            return Result.fail(ResultStatus.BUSINESS_ERROR);
        }
    }

    /**
     * 更新商品
     * @param productDTO DTO
     * @return
     */
    @PutMapping("/products/modify")
    @Operation(summary = "更新商品")
    public Result<ResultStatus> updateProduct(@RequestBody @Validated ProductDTO productDTO) {

        if(merchantProductService.updateProduct(productDTO)) {
            return Result.success(ResultStatus.UPDATED);
        }else{
            return Result.fail(ResultStatus.FAIL);
        }
    }

    /**
     * 删除商品
     * @param productId id
     * @return
     */
    @Operation(summary = "删除商品",description = "通过商品ID删除商品")
    @DeleteMapping("/products/{productId}")
    public Result<ResultStatus> deleteProduct(@PathVariable Long productId) {

        if(merchantProductService.removeProduct(productId)){
            return Result.success(ResultStatus.DELETED);
        }else {
            return Result.fail(ResultStatus.FAIL);
        }
    }

    /**
     * 更新上下架
     * @param productId productID
     * @param productStatus status
     * @return
     */
    @Operation(summary = "上下架更新",description = "更新上下架状态")
    @PutMapping("/products/{productId}/status")
    public Result<ResultStatus> updateProductStatus(@PathVariable Long productId,@RequestParam Integer productStatus) {

        if(merchantProductService.updateProductStatus(productId,productStatus)){
            return Result.success(ResultStatus.UPDATED);
        }else{
            return Result.fail(ResultStatus.FAIL);
        }
    }
}
