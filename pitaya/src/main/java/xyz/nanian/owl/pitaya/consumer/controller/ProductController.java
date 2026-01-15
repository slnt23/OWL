package xyz.nanian.owl.pitaya.consumer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.product.ProductApi;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

import java.util.List;

/**
 * 商品模块,
 * 采用的swagger是最新的springdoc版本，个别注解相较于springfox有不同，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Slf4j
@RestController
@RequestMapping("/pitaya/product/consumer")
@Tag(name = "消费者商品管理接口",description = "有关商品的的接口")
public class ProductController implements ProductApi{

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 分页查询，商品，总的搜索，不区分商家，
     * 本方法是查找商品列表的，
     * 但是这里使用PostMapping ,本意是为后续查找多条件预备，没有遵守restful ,要改
     * 方法错误，应该是使用list TODO 但是对于查询信息，真的最好是应用参数直接传送吗？
     * @param query 商品名信息
     * @return 分页及json结果，
     */
    @Override
    @PostMapping
    @Operation(summary = "商品列表",description = "不区分商户，分页查询")
    public Result<PageResult<ProductVO>> queryProductByName( @RequestBody @Validated ProductQuery query) {

//        log.info("开始查询商品信息");
        Page<ProductVO> page =productService.listProduct(query);
        PageResult<ProductVO> pageResult = PageResult.create(page);

        return Result.success(pageResult);
    }


    /**
     * 商品分类
     * @return json商品分类数据
     */
    @Override
    @Operation(summary = "商品分类",description = "商品的类别")
    @GetMapping("/category")
    public Result<List<CategoryVO>> queryCategory() {

        List<CategoryVO> category = productService.listCategory();

        return Result.success(category);
    }

    @Override
    @GetMapping("/detail")
    @Operation(summary= "商品详情",description = "单个商品的详细信息")
    @BizLog(module = "商品详情",action = "查询商品详情")
    public Result<ProductDetailVO> queryProductDetail(@RequestParam Integer productId) {

        ProductDetailVO productDetailVO =productService.getProductDetail(productId);

        return Result.success(productDetailVO);
    }

    @Override
    public Result<ProductVO> queryProductByName(String productName) {
        return null;
    }

//    @Override
//    public Result<PageResult<ProductVO>> queryProduct(String sellerId) {
//        return null;
//    }
//    @Override
//    public Result<String> modifyProductByName(Integer productId) {
//        return null;
//    }
//
//    @Override
//    public Integer addProductByName(String productName) {
//        return 0;
//    }
//
//    @Override
//    public Integer addProductImage(String productName, byte[] image) {
//        return 0;
//    }
//
//    @Override
//    public ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery) {
//        return null;
//    }
//
//    @Override
//    public Integer deleteProductByName(String productName) {
//        return 0;
//    }
//
//    @Override
//    public Integer modifyProductStatus() {
//        return 0;
//    }
}
