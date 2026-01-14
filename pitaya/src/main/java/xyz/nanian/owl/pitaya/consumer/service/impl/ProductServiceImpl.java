package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.pitaya.entity.CategoryDO;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.consumer.mapper.ProductMapper;
import xyz.nanian.owl.pitaya.consumer.mapstruct.ProductConvert;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;

import java.util.List;

/**
 * 商品Service
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Service
public class ProductServiceImpl implements ProductService {

    ProductMapper productMapper;
    ProductConvert productConvert;

    public ProductServiceImpl(ProductMapper productMapper, ProductConvert productConvert) {
        this.productMapper = productMapper;
        this.productConvert = productConvert;
    }


    /**
     * 用于分页查询 商品，
     * @param query 商品名，
     * @return List<productVO
     */
    @Override
    public Page<ProductVO> listProduct(ProductQuery query) {

        List<ProductDO> list = productMapper.listProduct(query.getProductName());

        List<ProductVO> voList =productConvert.productToVOList(list);

        Page<ProductVO> page =new Page<>();
        page.setRecords(voList);

        return page;
    }

    /**
     * 商品分类
     * @return list<商品分类VO
     */
    @Override
    public List<CategoryVO> listCategory() {

        List<CategoryDO> list = productMapper.selectCategory();
        List<CategoryVO> categoryVOList=productConvert.categoryToVOList(list);

        return categoryVOList;
    }

    @Override
    public ProductDetailVO getProductDetail(Integer productId) {

        ProductDO productDO=productMapper.selectProduct(productId);
        CategoryDO CategoryDO= productMapper.selectCategoryByProductId(productId);
        List<ProductImageDO> productImageDO = productMapper.selectProductImage(productId);

        ProductDetailVO productDetailVO =productConvert.detailToVO(productDO);
        productDetailVO.setImages(productConvert.detailToImageVOList(productImageDO));

        productDetailVO.setCategoryName(CategoryDO.getName());

        return productDetailVO;
    }
}
