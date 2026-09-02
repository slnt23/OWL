package xyz.nanian.owl.user.service;

import com.baomidou.mybatisplus.spring.service.IService;
import xyz.nanian.owl.user.domain.dto.GalleryDTO;
import xyz.nanian.owl.user.domain.entity.GalleryDO;
import xyz.nanian.owl.user.domain.vo.GalleryVO;
import xyz.nanian.owl.common.result.ResultPage;

import java.util.List;

/**
 * <p>
 * 画廊表 服务类
 * </p>
 *
 * @author slnt23
 * @since 2026-09-02
 */
public interface GalleryService extends IService<GalleryDO> {

    List<GalleryVO> listByUserId(Long userId);

    ResultPage<GalleryVO> pageByUserId(Long userId, long pageNum, long pageSize);

    ResultPage<GalleryVO> pageAll(long pageNum, long pageSize);

    GalleryVO getById(Long id, Long userId);

    int create(GalleryDTO dto, Long userId);

    Boolean update(GalleryDTO dto, Long userId);

    Boolean deleteById(Long id, Long userId);
}