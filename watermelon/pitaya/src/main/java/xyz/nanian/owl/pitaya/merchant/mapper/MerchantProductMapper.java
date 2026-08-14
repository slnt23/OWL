package xyz.nanian.owl.pitaya.merchant.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.domain.entity.ProductDO;
import xyz.nanian.owl.pitaya.domain.entity.ProductImageDO;

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

    /**
     * 插入商品
     * @param productDO
     * @return
     */
    Integer insertProductDO(ProductDO productDO);

    /**
     * 插入多张商品详情图片，
     * @param productImageDOList
     * @return
     */
    Integer insertProductImageList(@Param("list") List<ProductImageDO> productImageDOList);

    /**
     * 更新商品
     * @param productDO
     * @return
     */
    Integer updateProductDO(ProductDO productDO);

//    这里以一个删除+ 插入替代这个更新
//    Integer updateProductImageList(
//            @Param("productId") Long productId,
//            @Param("images") List<ProductImageDO> productImageDOList);

    /**
     * 删除商品详情图片
     * @param productId
     * @return
     */
    Integer deleteProductImageDO(@Param("productId") Long productId);

    Integer deleteProductDO(@Param("productId") Long productId);

    Integer updateProductStatus(@Param("productId") Long productId, @Param("status") Integer status);
}
