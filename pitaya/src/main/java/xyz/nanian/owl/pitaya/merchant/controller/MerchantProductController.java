package xyz.nanian.owl.pitaya.merchant.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.dto.ProductDTO;
import xyz.nanian.owl.pitaya.merchant.order.ProductApi;
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
@Tag(name = "商家商品管理接口",description = "商家商品")
public class MerchantProductController implements ProductApi {

    MerchantProductService merchantProductService;

    public MerchantProductController(MerchantProductService merchantProductService) {
        this.merchantProductService = merchantProductService;
    }

    /**
     * 新增商品
     * @param productDTO DTO
     * @return
     */
    @Override
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
    @Override
    @PutMapping("/products/modify")
    @Operation(summary = "更新商品")
    public Result<ResultStatus> updateProduct(@RequestBody @Validated ProductDTO productDTO) {

        if(merchantProductService.updateProduct(productDTO)) {
            return Result.success(ResultStatus.UPDATED);
        }else{
            return Result.fail(ResultStatus.FAIL);
        }
    }

    @Override
    public Result<ResultStatus> deleteProduct(Integer productId) {
        return null;
    }

    @Override
    public Result<ResultStatus> updateProductStatus(Integer productId, Integer productStatus) {
        return null;
    }
}
