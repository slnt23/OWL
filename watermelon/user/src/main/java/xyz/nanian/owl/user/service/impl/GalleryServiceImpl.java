package xyz.nanian.owl.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.result.ResultPage;
import xyz.nanian.owl.infra.minio.constant.MinioConstant;
import xyz.nanian.owl.infra.minio.service.FileStorageService;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.log.constant.LogType;
import xyz.nanian.owl.user.domain.dto.GalleryDTO;
import xyz.nanian.owl.user.domain.entity.GalleryDO;
import xyz.nanian.owl.user.domain.vo.GalleryVO;
import xyz.nanian.owl.user.mapper.GalleryMapper;
import xyz.nanian.owl.user.mapstruct.GalleryConvert;
import xyz.nanian.owl.user.service.GalleryService;

import java.util.List;

/**
 * <p>
 * 画廊表 服务实现类
 * </p>
 *
 * @author slnt23
 * @since 2026-09-02
 */
@Service
@RequiredArgsConstructor
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, GalleryDO> implements GalleryService {

    private final GalleryMapper galleryMapper;
    private final GalleryConvert galleryConvert;
    private final FileStorageService fileStorageService;

    @Override
    public List<GalleryVO> listByUserId(Long userId) {
        List<GalleryDO> list = galleryMapper.selectListByUserId(userId);
        fillImageUrls(list);
        return galleryConvert.DOConvertVO(list);
    }

    @Override
    public ResultPage<GalleryVO> pageByUserId(Long userId, long pageNum, long pageSize) {
        pageNum = normalizePageNum(pageNum);
        pageSize = normalizePageSize(pageSize);

        Page<GalleryDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<GalleryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(GalleryDO::getUserId, userId)
               .orderByAsc(GalleryDO::getSortOrder);

        return buildResultPage(galleryMapper.selectPage(page, wrapper));
    }

    @Override
    public ResultPage<GalleryVO> pageAll(long pageNum, long pageSize) {
        pageNum = normalizePageNum(pageNum);
        pageSize = normalizePageSize(pageSize);

        Page<GalleryDO> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<GalleryDO> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(GalleryDO::getSortOrder);

        return buildResultPage(galleryMapper.selectPage(page, wrapper));
    }

    @Override
    public GalleryVO getById(Long id, Long userId) {
        GalleryDO galleryDO = galleryMapper.selectById(id);
        if (galleryDO == null || !galleryDO.getUserId().equals(userId)) {
            return null;
        }
        fillImageUrls(galleryDO);
        return galleryConvert.DOConvertVO(galleryDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "画廊管理", action = "新增画廊项", persist = true)
    public int create(GalleryDTO dto, Long userId) {
        GalleryDO galleryDO = galleryConvert.DTOConvertDO(dto);
        galleryDO.setUserId(userId);
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imageUrl = fileStorageService.upload(dto.getImage(), MinioConstant.BUCKET_IMAGES);
            galleryDO.setImageUrl(imageUrl);
        }
        if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
            String thumbnailUrl = fileStorageService.upload(dto.getThumbnail(), MinioConstant.BUCKET_IMAGES);
            galleryDO.setThumbnailUrl(thumbnailUrl);
        }
        return galleryMapper.insert(galleryDO);
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "画廊管理", action = "修改画廊项", persist = true)
    public Boolean update(GalleryDTO dto, Long userId) {
        GalleryDO existing = galleryMapper.selectById(dto.getId());
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }
        GalleryDO galleryDO = galleryConvert.DTOConvertDO(dto);
        galleryDO.setId(dto.getId());
        if (dto.getImage() != null && !dto.getImage().isEmpty()) {
            String imageUrl = fileStorageService.upload(dto.getImage(), MinioConstant.BUCKET_IMAGES);
            galleryDO.setImageUrl(imageUrl);
        }
        if (dto.getThumbnail() != null && !dto.getThumbnail().isEmpty()) {
            String thumbnailUrl = fileStorageService.upload(dto.getThumbnail(), MinioConstant.BUCKET_IMAGES);
            galleryDO.setThumbnailUrl(thumbnailUrl);
        }
        int result = galleryMapper.updateById(galleryDO);
        return result == 1;
    }

    @Override
    @OperationLog(type = LogType.ADMIN, module = "画廊管理", action = "删除画廊项", persist = true)
    public Boolean deleteById(Long id, Long userId) {
        GalleryDO existing = galleryMapper.selectById(id);
        if (existing == null || !existing.getUserId().equals(userId)) {
            return false;
        }
        int result = galleryMapper.deleteById(id);
        return result == 1;
    }

    private void fillImageUrls(GalleryDO galleryDO) {
        String imageUrl = galleryDO.getImageUrl();
        if (imageUrl != null) {
            String resultUrl = fileStorageService.getUrl(MinioConstant.BUCKET_IMAGES, imageUrl);
            galleryDO.setImageUrl(resultUrl);
        }
        String thumbnailUrl = galleryDO.getThumbnailUrl();
        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            String resultThumbnailUrl = fileStorageService.getUrl(MinioConstant.BUCKET_IMAGES, thumbnailUrl);
            galleryDO.setThumbnailUrl(resultThumbnailUrl);
        }
    }

    private void fillImageUrls(List<GalleryDO> list) {
        for (GalleryDO galleryDO : list) {
            fillImageUrls(galleryDO);
        }
    }

    private ResultPage<GalleryVO> buildResultPage(IPage<GalleryDO> result) {
        List<GalleryDO> records = result.getRecords();
        fillImageUrls(records);
        List<GalleryVO> voList = galleryConvert.DOConvertVO(records);

        ResultPage<GalleryVO> pageResult = new ResultPage<>();
        pageResult.setCurrentPage(result.getCurrent());
        pageResult.setPageSize(result.getSize());
        pageResult.setTotal(result.getTotal());
        pageResult.setTotalPage(result.getPages());
        pageResult.setRecords(voList);
        return pageResult;
    }

    private long normalizePageNum(long pageNum) {
        return pageNum <= 0 ? 1 : pageNum;
    }

    private long normalizePageSize(long pageSize) {
        if (pageSize <= 0) {
            return 10;
        }
        return Math.min(pageSize, 100);
    }
}