package xyz.nanian.owl.sugarcane.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import xyz.nanian.owl.log.logging.BizLog;
import xyz.nanian.owl.sugarcane.constant.CacheConstant;
import xyz.nanian.owl.sugarcane.domain.dto.ItemIntroDTO;
import xyz.nanian.owl.sugarcane.domain.vo.PriceItemVO;
import xyz.nanian.owl.sugarcane.mapper.ItemMapper;
import xyz.nanian.owl.sugarcane.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;

/**
 * <p>
 * 被定价物品表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
@RequiredArgsConstructor
public class ItemServiceImpl extends ServiceImpl<ItemMapper, ItemDO> implements ItemService {

    final ItemMapper itemMapper;

    @Override
    @BizLog(module = "sugarcane",action = "模糊分页搜索物品Item")
    @Cacheable(value = CacheConstant.ITEM_PAGE,
            key = "'page:' + #itemIntroDTO.pageNum + ':' + #itemIntroDTO.pageSize + ':' + #itemIntroDTO.itemName",
            sync = true)
    public IPage<PriceItemVO> getItemIntroList(ItemIntroDTO itemIntroDTO) {
        Page<PriceItemVO> pageItems = new Page<>(itemIntroDTO.getPageNum(), itemIntroDTO.getPageSize());
        IPage<PriceItemVO> result = itemMapper.selectPageItems(pageItems, itemIntroDTO.getItemName());

        if (result.getRecords() == null || result.getRecords().isEmpty()) {
            return null;
        }
        return result;
    }
}
