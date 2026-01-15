package xyz.nanian.owl.pitaya.consumer.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.entity.CategoryDO;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;

import java.util.List;

/**
 * 商品Mapper,
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Mapper
public interface ProductMapper {


    /**
     * 分页查询商品
     * @return List<商品>
     */
    List<ProductDO> listProduct(@Param("productName") String productName);

    /**
     * 商品分类
     * @return List<分类
     */
    List<CategoryDO> selectCategory();

    /**
     * 商品分类，通过商品ID
     * @param productId productID
     * @return category
     */
    CategoryDO selectCategoryByProductId(Integer productId);

    /**
     * 商品信息，通过商品ID查询
     * @param productId productID
     * @return productDO
     */
    ProductDO selectProduct(Integer productId);

    /**
     * 商品照片List ,通过商品ID查询
     * @param productId productID
     * @return productImage<list
     */
    List<ProductImageDO> selectProductImage(Integer productId);
}
