package xyz.nanian.owl.pitaya;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nanian.owl.pitaya.consumer.mapper.ProductMapper;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;

import java.util.List;

/**
 * 商品测试类
 *
 * @author slnt23
 * @since 2026/1/14
 */

@SpringBootTest
public class ProductTest {

    @Autowired
    ProductMapper productMapper;
    @Autowired
    ProductService productService;

    @Test
    public void selectProduct() {

//        String ProductName="南极人羽绒被";
//
//        List<ProductDO> list= productMapper.listProduct(ProductName);
//
//        System.out.println(list);
    }

    @Test
    public void testCategory(){

        List<CategoryVO> category = productService.listCategory();
        System.out.println(category);

    }

    @Test
    public void testProductDetail(){
        ProductDetailVO productDetailVO =productService.getProductDetail(3);
        System.out.println(productDetailVO);
    }


}
