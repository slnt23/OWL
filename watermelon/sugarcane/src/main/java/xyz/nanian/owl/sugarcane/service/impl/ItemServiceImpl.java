package xyz.nanian.owl.sugarcane.service.impl;

import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import xyz.nanian.owl.sugarcane.mapper.ItemMapper;
import xyz.nanian.owl.sugarcane.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 被定价物品表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemDO> implements ItemService {

}
