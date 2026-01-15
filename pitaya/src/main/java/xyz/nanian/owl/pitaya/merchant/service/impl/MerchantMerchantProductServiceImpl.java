package xyz.nanian.owl.pitaya.merchant.service.impl;


import org.springframework.stereotype.Service;
import xyz.nanian.owl.pitaya.dto.ProductDTO;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;
import xyz.nanian.owl.pitaya.mapstruct.ProductConvert;
import xyz.nanian.owl.pitaya.merchant.mapper.MerchantProductMapper;
import xyz.nanian.owl.pitaya.merchant.service.MerchantProductService;

import java.util.List;

/**
 * 商家商品类实现
 *
 * @author slnt23
 * @since 2026/1/15
 */

@Service
public class MerchantMerchantProductServiceImpl implements MerchantProductService {

    MerchantProductMapper merchantProductMapper;
    ProductConvert productConvert ;

    public MerchantMerchantProductServiceImpl(MerchantProductMapper merchantProductMapper, ProductConvert productConvert) {
        this.productConvert = productConvert;
        this.merchantProductMapper = merchantProductMapper;
    }

    @Override
    public Boolean saveProduct(ProductDTO productDTO) {

//        DTO-》DO
        ProductDO productDO=productConvert.productToDO(productDTO);
        List<ProductImageDO> productImageDOList=productConvert.productImageDOList(productDTO.getImages());

//        Mapper
        Integer intProductNum = merchantProductMapper.insertProductDO(productDO);
        Integer intProductImageNum = merchantProductMapper.insertProductImageList(productImageDOList);

        if( intProductNum> 0 || intProductImageNum>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public Boolean updateProduct(ProductDTO productDTO) {


        return null;
    }

    @Override
    public Boolean removeProduct(Integer productId) {


        return null;
    }

    @Override
    public Boolean updateProductStatus(Integer productId, String status) {



        return null;
    }
}
