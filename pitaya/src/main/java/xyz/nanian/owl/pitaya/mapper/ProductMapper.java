package xyz.nanian.owl.pitaya.mapper;


import org.mapstruct.Mapper;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.query.ProductQuery;

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
    List<ProductDO> listProduct(String ProductName);
}
