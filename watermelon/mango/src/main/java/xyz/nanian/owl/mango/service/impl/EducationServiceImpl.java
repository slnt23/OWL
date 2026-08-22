package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.common.exception.BizException;
import xyz.nanian.owl.common.result.ResultStatus;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.EducationDTO;
import xyz.nanian.owl.mango.domain.entity.BlogEducationDO;
import xyz.nanian.owl.mango.domain.vo.EducationVO;
import xyz.nanian.owl.mango.mapper.BlogEducationMapper;
import xyz.nanian.owl.mango.mapstruct.EducationConvert;
import xyz.nanian.owl.mango.service.EducationService;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 博客教育经历服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final BlogEducationMapper blogEducationMapper;
    private final EducationConvert educationConvert;

    @Override
    public List<EducationVO> list() {
        List<BlogEducationDO> list = blogEducationMapper.selectList(Wrappers.<BlogEducationDO>lambdaQuery()
                .orderByAsc(BlogEducationDO::getSortOrder)
                .orderByAsc(BlogEducationDO::getId));
        return educationConvert.toVOList(list);
    }

    @Override
    @OperationLog(module = "博客", action = "新增教育经历", persist = true)
    public Long create(EducationDTO dto) {
        LocalDateTime now = LocalDateTime.now();
        BlogEducationDO education = new BlogEducationDO();
        education.setSchool(dto.getSchool().trim());
        education.setDegree(dto.getDegree().trim());
        education.setPeriod(dto.getPeriod().trim());
        education.setSortOrder(dto.getSortOrder() == null ? 0 : dto.getSortOrder());
        education.setCreateTime(now);
        education.setUpdateTime(now);
        blogEducationMapper.insert(education);
        return education.getId();
    }

    @Override
    @OperationLog(module = "博客", action = "更新教育经历", persist = true)
    public Boolean update(Long id, EducationDTO dto) {
        requireEducation(id);

        LambdaUpdateWrapper<BlogEducationDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogEducationDO::getId, id);
        boolean changed = false;
        if (dto.getSchool() != null && !dto.getSchool().isBlank()) {
            wrapper.set(BlogEducationDO::getSchool, dto.getSchool().trim());
            changed = true;
        }
        if (dto.getDegree() != null && !dto.getDegree().isBlank()) {
            wrapper.set(BlogEducationDO::getDegree, dto.getDegree().trim());
            changed = true;
        }
        if (dto.getPeriod() != null && !dto.getPeriod().isBlank()) {
            wrapper.set(BlogEducationDO::getPeriod, dto.getPeriod().trim());
            changed = true;
        }
        if (dto.getSortOrder() != null) {
            wrapper.set(BlogEducationDO::getSortOrder, dto.getSortOrder());
            changed = true;
        }
        return !changed || blogEducationMapper.update(null, wrapper) > 0;
    }

    @Override
    @OperationLog(module = "博客", action = "删除教育经历", persist = true)
    public Boolean deleteById(Long id) {
        requireEducation(id);
        return blogEducationMapper.deleteById(id) > 0;
    }

    private BlogEducationDO requireEducation(Long id) {
        BlogEducationDO education = blogEducationMapper.selectById(id);
        if (education == null) {
            throw new BizException(ResultStatus.NOT_FOUND);
        }
        return education;
    }
}
