package xyz.nanian.owl.pitaya.consumer.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.domain.vo.CategoryVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductVO;
import xyz.nanian.owl.result.ResultPage;
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
@Tag(name = "消费者商品管理",description = "有关商品的的接口")
public class ProductController{

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 查询商品，分页，总搜索栏，
     * 通过商品名，
     *
     * @param productName 商品名信息
     * @return 分页及json结果
     */
    @GetMapping("/products")
    @Operation(summary = "商品列表",description = "不区分商户，分页查询")
    public Result<ResultPage<ProductVO>> queryProduct(@RequestParam String productName,
                                                      @RequestParam Integer pageNum,
                                                      @RequestParam Integer pageSize) {

//        这里缺少对商品的检验，但是标准没有指定，暂时搁置

//        log.info("开始查询商品信息");
        ResultPage<ProductVO> result =productService.listProduct(productName,pageNum,pageSize);

        return Result.success(result);
    }


    /**
     * 商品分类
     * @return json商品分类数据
     */
    @Operation(summary = "商品分类",description = "商品的类别")
    @GetMapping("/category")
    public Result<List<CategoryVO>> queryCategory() {

        List<CategoryVO> category = productService.listCategory();

        return Result.success(category);
    }

    @GetMapping("/detail")
    @Operation(summary= "商品详情",description = "单个商品的详细信息")
    @BizLog(module = "商品详情",action = "查询商品详情")
    public Result<ProductDetailVO> queryProductDetail(@RequestParam Integer productId) {

        ProductDetailVO productDetailVO =productService.getProductDetail(productId);

        return Result.success(productDetailVO);
    }

//    @Override
//    public Result<ProductVO> queryProductByName(String productName) {
//        return null;
//    }

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
