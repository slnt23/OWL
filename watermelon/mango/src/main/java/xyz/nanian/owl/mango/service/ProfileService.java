package xyz.nanian.owl.mango.service;

import xyz.nanian.owl.mango.domain.dto.ProfileUpdateDTO;
import xyz.nanian.owl.mango.domain.vo.ProfileVO;

/**
 * 博客站长个人信息服务
 *
 * @author slnt23
 * @since 2026/8/22
 */
public interface ProfileService {

    /**
     * 获取站长信息（单行，id 固定为 1）
     *
     * @return 站长信息；未初始化时返回 null
     */
    ProfileVO get();

    /**
     * 更新站长信息
     *
     * @param dto 入参
     * @return 是否更新成功
     */
    Boolean update(ProfileUpdateDTO dto);
}
