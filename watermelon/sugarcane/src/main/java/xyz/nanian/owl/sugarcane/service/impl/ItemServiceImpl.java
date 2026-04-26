package xyz.nanian.owl.sugarcane.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.sugarcane.domain.dto.ItemIntroDTO;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import xyz.nanian.owl.sugarcane.domain.vo.ItemIntroVO;
import xyz.nanian.owl.sugarcane.mapper.ItemMapper;
import xyz.nanian.owl.sugarcane.mapstruct.ItemConvert;
import xyz.nanian.owl.sugarcane.service.ItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
    final ItemConvert itemConvert;

    @Override
    public IPage<ItemIntroVO> getItemIntroList(ItemIntroDTO itemIntroDTO) {

//        分页查询，
        Page<ItemDO> pageItems= new Page<>(itemIntroDTO.getPageNum(),itemIntroDTO.getPageSize());
        IPage<ItemDO> itemDOIPage = itemMapper.selectPageItems(pageItems,itemIntroDTO.getItemName());

//        仅仅转换list
        List<ItemIntroVO> itemIntroVOList = itemConvert.DOtoVO(itemDOIPage.getRecords());

        IPage<ItemIntroVO> itemIntroVOIPage = new Page<>(itemIntroDTO.getPageNum(),itemIntroDTO.getPageSize());
        itemIntroVOIPage.setRecords(itemIntroVOList);
        itemIntroVOIPage.setTotal(itemDOIPage.getTotal());

        return itemIntroVOIPage;
    }
}
