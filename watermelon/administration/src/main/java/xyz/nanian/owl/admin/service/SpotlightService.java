package xyz.nanian.owl.admin.service;

import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.entity.SpotlightDO;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;

import java.util.List;

/**
 * <p>
 * 首页焦点展示项目表 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-04-24 17:13:37
 */
public interface SpotlightService extends IService<SpotlightDO> {
    List<SpotlightVO> listByOrder();
    SpotlightVO getById(Integer id);
    SpotlightVO create(SpotlightDTO dto);
    SpotlightVO update(SpotlightDTO dto);
    void deleteById(Integer id);

}
