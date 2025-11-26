package xyz.nanian.owl.pitaya.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.service.ProductService;
import xyz.nanian.owl.pitaya.vo.ProductVO;

/**
 * 商品Service
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Service
public class ProductServiceImpl implements ProductService {


    @Override
    public Page<ProductVO> listProduct(ProductQuery query) {
        return null;
    }
}
