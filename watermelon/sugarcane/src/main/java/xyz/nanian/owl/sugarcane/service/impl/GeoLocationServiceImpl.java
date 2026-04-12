package xyz.nanian.owl.sugarcane.service.impl;

import xyz.nanian.owl.sugarcane.entity.GeoLocationDO;
import xyz.nanian.owl.sugarcane.mapper.GeoLocationMapper;
import xyz.nanian.owl.sugarcane.service.GeoLocationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 地理位置表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-12 20:43:32
 */
@Service
public class GeoLocationServiceImpl extends ServiceImpl<GeoLocationMapper, GeoLocationDO> implements GeoLocationService {

}
