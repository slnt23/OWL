package xyz.nanian.owl.pitaya.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.mapper.ProductMapper;
import xyz.nanian.owl.pitaya.mapstruct.ProductConvert;
import xyz.nanian.owl.pitaya.query.ProductQuery;
import xyz.nanian.owl.pitaya.service.ProductService;
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

        List<ProductVO> voList =productConvert.toVOList(list);

        Page<ProductVO> page =new Page<>();
        page.setRecords(voList);
//        page.setTotal(list.size());

        return page;
    }
}
