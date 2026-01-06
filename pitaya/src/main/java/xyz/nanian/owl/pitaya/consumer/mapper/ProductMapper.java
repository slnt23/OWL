package xyz.nanian.owl.pitaya.consumer.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.pitaya.consumer.entity.ProductDO;

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
}
