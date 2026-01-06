package xyz.nanian.owl.pitaya.consumer.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import xyz.nanian.owl.pitaya.consumer.product.ProductApi;
import xyz.nanian.owl.pitaya.consumer.query.ProductQuery;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.consumer.vo.CategoryVO;
import xyz.nanian.owl.pitaya.consumer.vo.ProductVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

/**
 * 商品模块,
 * 采用的swagger是最新的springdoc版本，个别注解相较于springfox有不同，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Slf4j
@RestController
@RequestMapping("/pitaya/product")
@Tag(name = "商品管理接口",description = "有关商品的的接口")
public class ProductController implements ProductApi{

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 分页查询，商品，总的搜索，不区分商家，
     * 本方法是查找信息的，但是这里使用PostMapping ,本意是为后续查找多条件预备，
     * TODO 但是对于查询信息，真的最好是应用参数直接传送吗？
     * @param query 商品名信息
     * @return 分页及json结果，
     */
    @Override
    @PostMapping
    @Operation(summary = "分页查询商品",description = "不区分商户")
    public Result<PageResult<ProductVO>> queryProductByName(@RequestBody @Validated ProductQuery query) {

        log.info("开始查询商品信息");
        Page<ProductVO> page =productService.listProduct(query);
        PageResult<ProductVO> pageResult = PageResult.create(page);

        return Result.success(pageResult);
    }

    @Override
    public Result<PageResult<ProductVO>> queryProduct(String sellerId) {
        return null;
    }

    @Override
    public Result<PageResult<CategoryVO>> queryCategory() {
        return null;
    }

    @Override
    public Integer modifyProductByName(String productName) {
        return 0;
    }

    @Override
    public Integer addProductByName(String productName) {
        return 0;
    }

    @Override
    public Integer addProductImage(String productName, byte[] image) {
        return 0;
    }

    @Override
    public ResponseEntity<byte[]> exportProductInfo(ProductQuery productQuery) {
        return null;
    }

    @Override
    public Integer deleteProductByName(String productName) {
        return 0;
    }

    @Override
    public Integer modifyProductStatus() {
        return 0;
    }
}
