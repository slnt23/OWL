package xyz.nanian.owl.pitaya;


import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.mapper.ProductMapper;

import java.util.List;

/**
 * 有关商品的测试 TODO 添加数据，
 *
 * @author slnt23
 * @since 2025/11/26
 */

@SpringBootApplication
public class ProductTest {

    ProductMapper mapper;

    ProductTest(ProductMapper productMapper) {

        this.mapper = productMapper;
    }

    @Test
    public void selectProduct() {
        String ProductName="火龙果";

        ProductDO productDO = new ProductDO();
        List<ProductDO> list= mapper.listProduct(ProductName);

        System.out.println(list);
    }
}
