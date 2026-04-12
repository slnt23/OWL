package xyz.nanian.owl.sugarcane.service.impl;

import xyz.nanian.owl.sugarcane.entity.CategoryDO;
import xyz.nanian.owl.sugarcane.mapper.CategoryMapper;
import xyz.nanian.owl.sugarcane.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 价格系统分类表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryDO> implements CategoryService {

}
