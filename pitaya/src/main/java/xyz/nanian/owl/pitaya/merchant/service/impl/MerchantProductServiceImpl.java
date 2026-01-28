package xyz.nanian.owl.pitaya.merchant.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.logging.BizLog;
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

@Slf4j
@Service
public class MerchantProductServiceImpl implements MerchantProductService {

    MerchantProductMapper merchantProductMapper;
    ProductConvert productConvert ;

    public MerchantProductServiceImpl(MerchantProductMapper merchantProductMapper, ProductConvert productConvert) {
        this.productConvert = productConvert;
        this.merchantProductMapper = merchantProductMapper;
    }

    /**
     * 新增商品
     * @param productDTO DTO
     * @return 0/1
     */
    @Override
    @BizLog(module = "商品",action = "新增商品")
    public Boolean saveProduct(ProductDTO productDTO) {

//        DTO-》DO
        ProductDO productDO=productConvert.productToDO(productDTO);
        List<ProductImageDO> productImageDOList=productConvert.productImageDOList(productDTO.getImages());

//        Mapper
        Integer intProductNum = merchantProductMapper.insertProductDO(productDO);
        Integer intProductImageNum = merchantProductMapper.insertProductImageList(productImageDOList);

        return intProductNum > 0 || intProductImageNum > 0;
    }

    /**
     * 更新商品
     * @param productDTO DTO
     * @return
     */
    @Override
    @BizLog(module = "商品",action = "更新商品")
    public Boolean updateProduct(ProductDTO productDTO) {
        ProductDO productDO=productConvert.productToDO(productDTO);
        List<ProductImageDO> productImageDOList=productConvert.productImageDOList(productDTO.getImages());

        Integer intProductNum = merchantProductMapper.updateProductDO(productDO);
        Long productId = productDO.getId();
        Integer intDeleteProductImage = merchantProductMapper.deleteProductImageDO(productId);
        Integer intProductImageNum = merchantProductMapper.insertProductImageList(productImageDOList);

        return intProductNum > 0 || intProductImageNum > 0;
    }

    /**
     * 删除商品
     * @param productId
     * @return
     */
    @Override
    @BizLog(module = "商品",action = "删除商品")
    public Boolean removeProduct(Long productId) {

        Integer intProductImage = merchantProductMapper.deleteProductImageDO(productId);
        Integer intProductNum = merchantProductMapper.deleteProductDO(productId);

        log.info("删除影响行数（图，商品）：{}，{}",intProductImage,intProductNum);
        return intProductNum >= 0 || intProductImage >= 0;
    }

    /**
     * 更新上下架状态
     * @param productId
     * @param status
     * @return
     */
    @Override
    @BizLog(module = "商品",action = "商品上下架更新状态")
    public Boolean updateProductStatus(Long productId, Integer status) {

        Integer intUpdateStatus=merchantProductMapper.updateProductStatus(productId,status);

        return intUpdateStatus > 0;
    }
}
