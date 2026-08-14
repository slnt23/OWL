package xyz.nanian.owl.pitaya.consumer.mapper;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.domain.entity.CategoryDO;
import xyz.nanian.owl.pitaya.domain.entity.ProductDO;
import xyz.nanian.owl.pitaya.domain.entity.ProductImageDO;
import xyz.nanian.owl.pitaya.domain.vo.ProductVO;

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
     * 查询商品,分页
     * @return List<商品>
     */
    IPage<ProductVO> pageProduct(Page<?> page,
                                 @Param("productName") String productName);

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
