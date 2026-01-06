package xyz.nanian.owl.pitaya.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.nanian.owl.pitaya.consumer.query.ProductQuery;
import xyz.nanian.owl.pitaya.consumer.vo.ProductVO;

/**
 * 商品模块
 *
 * @author slnt23
 * @since 2025/11/10
 */

public interface ProductService {

    /**
     * 通过商品名查询商品
     * @param query 商品名，
     * @return 分页商品信息，
     */
    Page<ProductVO> listProduct(ProductQuery query);
}
