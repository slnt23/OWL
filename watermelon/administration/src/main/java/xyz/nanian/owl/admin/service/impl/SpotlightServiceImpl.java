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
import xyz.nanian.owl.infrastructure.minio.constant.MinioConstant;
import xyz.nanian.owl.infrastructure.minio.service.FileStorageService;

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
    private final FileStorageService fileStorageService;

    @Override
    public List<SpotlightVO> listByOrder() {
        List<SpotlightDO> list = spotlightMapper.selectLists();

        return spotlightConvert.DOConvertVO(list);
    }

    @Override
    public SpotlightVO getById(Integer id) {
        SpotlightDO spotlightDO = spotlightMapper.selectById(id);
        return spotlightConvert.DOConvertVO(spotlightDO);
    }

    @Override
    public int create(SpotlightDTO dto) {
        SpotlightDO spotlightDO= spotlightConvert.DTOConvertDO(dto);
//        这里要把照片上传到OSS
        String imageUrl = fileStorageService.upload(dto.getImage(), MinioConstant.BUCKET_IMAGES);
        spotlightDO.setImageUrl(imageUrl);

        return spotlightMapper.insert(spotlightDO);
    }

    @Override
    public Boolean update(SpotlightDTO dto) {
        SpotlightDO spotlightDO = spotlightConvert.DTOConvertDO(dto);

//        这里如果照片更新，应该线删除，再上传，
//        先查询DO,获取DO中url，删除，商创，


        int result = spotlightMapper.updateById(spotlightDO);
        return result==1;
    }

    @Override
    public Boolean deleteById(Integer id) {
        int result = spotlightMapper.deleteById(id);
        return result == 1;
    }
}
