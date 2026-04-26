package xyz.nanian.owl.pitaya.consumer.service;


import xyz.nanian.owl.pitaya.domain.vo.CategoryVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductVO;
import xyz.nanian.owl.result.ResultPage;

import java.util.List;

/**
 * 商品模块
 *
 * @author slnt23
 * @since 2025/11/10
 */

public interface ProductService {

    /**
     * 查询商品,通过商品名，且使用分页，
     * @param productName 商品名，
     * @return 商品信息，
     */
    ResultPage<ProductVO> listProduct(String productName, Integer pageNum, Integer pageSize);

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
