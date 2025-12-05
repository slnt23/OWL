package xyz.nanian.owl.pitaya.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import xyz.nanian.owl.pitaya.product.ProductApi;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;
import xyz.nanian.owl.result.PageResult;
import xyz.nanian.owl.result.Result;

/**
 * 商品模块,
 * 采用的swagger是最新的springdoc版本，个别注解相较于springfox有不同，
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Controller("/pitaya/product")
@Tag(name = "商品管理接口",description = "有关商品的的接口")
public class ProductController implements ProductApi{


    /**
     * 分页查询，商品，总的搜索，不区分商家，
     * @param query 商品名信息
     * @return 分页及json结果，
     */
    @Override
    public Result<PageResult<ProductVO>> queryProductByName(ProductQuery query) {


        return null;
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
