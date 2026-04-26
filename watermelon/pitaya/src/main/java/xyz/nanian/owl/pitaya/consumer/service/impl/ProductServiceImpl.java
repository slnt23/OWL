package xyz.nanian.owl.pitaya.consumer.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.pitaya.domain.entity.CategoryDO;
import xyz.nanian.owl.pitaya.domain.entity.ProductDO;
import xyz.nanian.owl.pitaya.consumer.mapper.ProductMapper;
import xyz.nanian.owl.pitaya.mapstruct.ProductConvert;
import xyz.nanian.owl.pitaya.domain.entity.ProductImageDO;
import xyz.nanian.owl.pitaya.consumer.service.ProductService;
import xyz.nanian.owl.pitaya.domain.vo.CategoryVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductDetailVO;
import xyz.nanian.owl.pitaya.domain.vo.ProductVO;
import xyz.nanian.owl.result.ResultPage;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static xyz.nanian.owl.pitaya.constant.ShopConstant.*;

/**
 * 商品Service
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Service
public class ProductServiceImpl implements ProductService {

    ProductMapper productMapper;
    ProductConvert productConvert;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProductServiceImpl(ProductMapper productMapper,
                              ProductConvert productConvert,
                              RedisTemplate redisTemplate) {
        this.productMapper = productMapper;
        this.productConvert = productConvert;
        this.redisTemplate = redisTemplate;
    }


    /**
     * 查询商品，分页查询，
     * @param productName 商品名，
     * @return List<productVO
     */
    @Override
    @BizLog(module = "用户商品",action = "查询商品")
    public ResultPage<ProductVO> listProduct(String productName, Integer pageNum, Integer pageSize) {

        if(pageSize >50){
            pageSize = 50;
        }
        String key = CONSUME_PRODUCT_KEY + productName;
//        查询Redis
        ResultPage<ProductVO> cache=
                (ResultPage<ProductVO>) redisTemplate.opsForValue().get(key);

        if(cache!=null){
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            ResultPage<ProductVO> result = mapper.convertValue(cache, new TypeReference<ResultPage<ProductVO>>() {});
            return result;
        }

//        redis没有，数据库
        Page<ProductVO> page = new Page<>(pageNum,pageSize);
        IPage<ProductVO> result = productMapper.pageProduct(page,productName);

        ResultPage<ProductVO> resultPage = ResultPage.create(result);

//        写如Redis
        redisTemplate.opsForValue().set(key, resultPage,CONSUME_PRODUCT_TIME_OUT, TimeUnit.MINUTES);

        return resultPage;
    }

    /**
     * 商品分类
     * @return list<商品分类VO
     */
    @Override
    @BizLog(module = "用户商品",action = "查询商品分类")
    public List<CategoryVO> listCategory() {

        String key = CATEGORY_KEY;

//        查询Redis
        List<CategoryVO> cache = (List<CategoryVO>) redisTemplate.opsForValue().get(key);
        if(cache!=null){
            return cache;
        }

//        else没有，查询Mysql，
        List<CategoryDO> list = productMapper.selectCategory();
        List<CategoryVO> categoryVOList=productConvert.categoryToVOList(list);

        redisTemplate.opsForValue().set(key,categoryVOList,CATEGORY_TIME_OUT, TimeUnit.MINUTES);

        return categoryVOList;
    }

    /**
     * 商品详情
     * @param productId 商品ID
     * @return productDetailVO
     */
    @Override
    @BizLog(module = "用户商品",action = "查询商品详情")
    public ProductDetailVO getProductDetail(Integer productId) {

        String key = PRODUCT_DETAIL_KEY + productId;

        ProductDetailVO cache = (ProductDetailVO) redisTemplate.opsForValue().get(key);
        if(cache!=null){
            return cache;
        }

        ProductDO productDO=productMapper.selectProduct(productId);
        CategoryDO CategoryDO= productMapper.selectCategoryByProductId(productId);
        List<ProductImageDO> productImageDO = productMapper.selectProductImage(productId);

        ProductDetailVO productDetailVO =productConvert.detailToVO(productDO);
        productDetailVO.setImages(productConvert.detailToImageVOList(productImageDO));

        productDetailVO.setCategoryName(CategoryDO.getName());

        redisTemplate.opsForValue().set(key,productDetailVO,PRODUCT_DETAIL_TIME_OUT, TimeUnit.MINUTES);

        return productDetailVO;
    }
}
