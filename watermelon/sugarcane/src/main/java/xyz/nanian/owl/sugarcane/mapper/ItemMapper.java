package xyz.nanian.owl.sugarcane.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.sugarcane.domain.entity.ItemDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 被定价物品表 Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Mapper
public interface ItemMapper extends BaseMapper<ItemDO> {

    IPage<ItemDO> selectPageItems(Page<?> page,@Param("itemName") String itemName);

}
