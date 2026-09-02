package xyz.nanian.owl.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.nanian.owl.user.domain.entity.GalleryDO;

import java.util.List;

/**
 * <p>
 * 画廊表 Mapper 接口
 * </p>
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Mapper
public interface GalleryMapper extends BaseMapper<GalleryDO> {

    List<GalleryDO> selectListByUserId(@Param("userId") Long userId);
}