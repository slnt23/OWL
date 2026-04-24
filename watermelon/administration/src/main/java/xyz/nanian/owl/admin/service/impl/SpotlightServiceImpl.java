package xyz.nanian.owl.admin.service.impl;

import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.admin.convert.SpotlightConvert;
import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.entity.SpotlightDO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;
import xyz.nanian.owl.admin.mapper.SpotlightMapper;
import xyz.nanian.owl.admin.service.SpotlightService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 首页焦点展示项目表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
@Service
@RequiredArgsConstructor
public class SpotlightServiceImpl extends ServiceImpl<SpotlightMapper, SpotlightDO> implements SpotlightService {

    final SpotlightMapper spotlightMapper;
    final SpotlightConvert spotlightConvert;

    @Override
    public List<SpotlightVO> listByOrder() {
        List<SpotlightDO> list = spotlightMapper.selectLists();

        return spotlightConvert.DOConvertVO(list);
    }

    @Override
    public SpotlightVO getById(Integer id) {
        return null;
    }

    @Override
    public SpotlightVO create(SpotlightDTO dto) {
        return null;
    }

    @Override
    public SpotlightVO update(SpotlightDTO dto) {
        return null;
    }

    @Override
    public void deleteById(Integer id) {

    }
}
