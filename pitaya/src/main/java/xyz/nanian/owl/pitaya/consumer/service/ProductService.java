package xyz.nanian.owl.pitaya.consumer.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;

import java.util.List;

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

    /**
     * 商品分类
     * @return 分类列表
     */
    List<CategoryVO> listCategory();

    /**
     * 商品详情
     * @param productId productId
     * @return detailVO
     */
    ProductDetailVO getProductDetail(Integer productId);
}
