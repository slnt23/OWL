package xyz.nanian.owl.pitaya.merchant.controller;


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

    @Override
    @BizLog(module= "商家商品",action = "新增商品")
    @PostMapping("/products")
    public Result<ResultStatus> addProduct(@RequestBody @Validated ProductDTO productDTO) {

        if(merchantProductService.saveProduct(productDTO)) {
            return Result.success(ResultStatus.CREATED);
        }else {
            return Result.fail(ResultStatus.BUSINESS_ERROR);
        }
    }

    @Override
    @BizLog(module = "商家商品",action = "更新商品")
    public Result<ResultStatus> updateProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    @BizLog(module = "商家商品",action = "删除商品")
    public Result<ResultStatus> deleteProduct(Integer productId) {
        return null;
    }

    @Override
    @BizLog(module = "商家商品",action = "更新上下架状态")
    public Result<ResultStatus> updateProductStatus(Integer productId, Integer productStatus) {
        return null;
    }
}
