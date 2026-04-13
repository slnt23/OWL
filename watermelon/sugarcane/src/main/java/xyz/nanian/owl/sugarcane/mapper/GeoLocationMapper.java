package xyz.nanian.owl.sugarcane.mapper;

import xyz.nanian.owl.sugarcane.domain.entity.GeoLocationDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 地理位置表 Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Mapper
public interface GeoLocationMapper extends BaseMapper<GeoLocationDO> {

}
