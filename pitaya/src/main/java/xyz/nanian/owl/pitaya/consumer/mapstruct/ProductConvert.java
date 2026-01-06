package xyz.nanian.owl.pitaya.consumer.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.pitaya.consumer.entity.ProductDO;
import xyz.nanian.owl.pitaya.consumer.vo.ProductVO;

import java.util.List;

/**
 * 有关商品的类映射
 *
 * @author slnt23
 * @since 2025/12/6
 */

@Mapper(componentModel = "spring")
public interface ProductConvert {

//    ProductConvert INSTANCE = Mappers.getMapper(ProductConvert.class);


    @Mapping(source = "name",target = "productName")
    ProductVO toVO(ProductDO productDO);

    List<ProductVO> toVOList(List<ProductDO> productDOList);
}
