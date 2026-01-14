package xyz.nanian.owl.pitaya.consumer.mapstruct;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import xyz.nanian.owl.pitaya.entity.CategoryDO;
import xyz.nanian.owl.pitaya.entity.ProductDO;
import xyz.nanian.owl.pitaya.entity.ProductImageDO;
import xyz.nanian.owl.pitaya.vo.CategoryVO;
import xyz.nanian.owl.pitaya.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.vo.ProductVO;

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
    ProductVO productToVO(ProductDO productDO);

    List<ProductVO> productToVOList(List<ProductDO> productDOList);

    CategoryVO categoryToVO(CategoryDO categoryDO);

    List<CategoryVO> categoryToVOList(List<CategoryDO> categoryDOList);

    ProductDetailVO detailToVO(ProductDO productDO);

    @Mapping(source = "sort",target = "imageSort")
    ProductDetailVO.ProductImageVO detailToImageVO(ProductImageDO productImageDO);

    List<ProductDetailVO.ProductImageVO>  detailToImageVOList(List<ProductImageDO> productImageDOList);
}
