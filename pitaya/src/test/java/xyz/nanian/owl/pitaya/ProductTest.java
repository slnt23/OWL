package xyz.nanian.owl.pitaya;


import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.mapper.ProductMapper;

import java.util.List;

/**
 * 有关商品的测试
 *
 * @author slnt23
 * @since 2025/11/26
 */

@Slf4j
@SpringBootTest
public class ProductTest {

    @Resource
    ProductMapper mapper;

    @Test
    public void selectProduct() {
        String ProductName="南极人羽绒被";

//        ProductDO productDO = new ProductDO();
        List<ProductDO> list= mapper.listProduct(ProductName);

        System.out.println(list);
    }
}
