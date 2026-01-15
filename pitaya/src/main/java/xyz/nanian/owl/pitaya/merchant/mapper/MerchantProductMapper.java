package xyz.nanian.owl.pitaya.merchant.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;

import java.util.List;

/**
 * 商家商品Mapper
 * BaseMapper 只是针对单张表的，不可以多张表
 *
 * @author slnt23
 * @since 2026/1/15
 */

@Mapper
public interface MerchantProductMapper {

    Integer insertProductDO(ProductDO productDO);

    Integer insertProductImageList(@Param("list") List<ProductImageDO> productImageDOList);


}
