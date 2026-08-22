package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.mango.domain.dto.EducationDTO;
import xyz.nanian.owl.mango.domain.vo.EducationVO;

import java.util.List;

/**
 * 博客教育经历服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface EducationService {

    /**
     * 全部教育经历
     *
     * @return 教育经历列表
     */
    List<EducationVO> list();

    /**
     * 新增教育经历
     *
     * @param dto 入参
     * @return 新经历 ID
     */
    Long create(EducationDTO dto);

    /**
     * 更新教育经历
     *
     * @param id  经历 ID
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean update(Long id, EducationDTO dto);

    /**
     * 删除教育经历
     *
     * @param id 经历 ID
     * @return 是否删除成功
     */
    Boolean deleteById(Long id);
}
