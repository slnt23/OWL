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

    CategoryDO selectCategoryByProductId(Integer productId);

    ProductDO selectProduct(Integer productId);

    List<ProductImageDO> selectProductImage(Integer productId);
}
