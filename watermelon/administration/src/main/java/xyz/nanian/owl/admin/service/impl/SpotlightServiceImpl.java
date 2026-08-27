package xyz.nanian.owl.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import xyz.nanian.owl.admin.convert.SpotlightConvert;
import xyz.nanian.owl.admin.domain.dto.SpotlightDTO;
import xyz.nanian.owl.admin.domain.entity.SpotlightDO;
import xyz.nanian.owl.admin.domain.vo.SpotlightVO;
import xyz.nanian.owl.admin.mapper.SpotlightMapper;
import xyz.nanian.owl.admin.service.SpotlightService;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.infra.minio.constant.MinioConstant;
import xyz.nanian.owl.infra.minio.service.FileStorageService;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;

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

        for(SpotlightDO spotlightDO : list) {
            String imageUrl = spotlightDO.getImageUrl();

            String resultUrl = fileStorageService.getUrl(MinioConstant.BUCKET_IMAGES,imageUrl);
            spotlightDO.setImageUrl(resultUrl);
        }

        return spotlightConvert.DOConvertVO(list);
    }

    @Override
    public ResultPage<SpotlightVO> page(long pageNum, long pageSize) {
        if (pageNum <= 0) {
            pageNum = 1;
        }
        if (pageSize <= 0) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }

        Page<SpotlightDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SpotlightDO> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(SpotlightDO::getSortOrder);

        IPage<SpotlightDO> result = spotlightMapper.selectPage(page, wrapper);
        List<SpotlightDO> records = result.getRecords();
        for (SpotlightDO spotlightDO : records) {
            String imageUrl = spotlightDO.getImageUrl();
            String resultUrl = fileStorageService.getUrl(MinioConstant.BUCKET_IMAGES, imageUrl);
            spotlightDO.setImageUrl(resultUrl);
        }
        List<SpotlightVO> voList = spotlightConvert.DOConvertVO(records);

        ResultPage<SpotlightVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }

    @Override
    public SpotlightVO getById(Long id) {
        SpotlightDO spotlightDO = spotlightMapper.selectById(id);
        return spotlightConvert.DOConvertVO(spotlightDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "新增焦点项目", persist = true)
    public int create(SpotlightDTO dto) {
        SpotlightDO spotlightDO= spotlightConvert.DTOConvertDO(dto);
//        这里要把照片上传到OSS
        String imageUrl = fileStorageService.upload(dto.getImage(), MinioConstant.BUCKET_IMAGES);
        spotlightDO.setImageUrl(imageUrl);

        return spotlightMapper.insert(spotlightDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "修改焦点项目", persist = true)
    public Boolean update(SpotlightDTO dto) {
        SpotlightDO spotlightDO = spotlightConvert.DTOConvertDO(dto);
        spotlightDO.setId(dto.getId());
//        这里如果照片更新，应该线删除，再上传，
//        先查询DO,获取DO中url，删除，商创，
//        或者直接将原来的都删掉，新建一个，

        int result = spotlightMapper.updateById(spotlightDO);
        return result==1;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "首页配置", action = "删除焦点项目", persist = true)
    public Boolean deleteById(Long id) {
        int result = spotlightMapper.deleteById(id);
        return result == 1;
    }
}