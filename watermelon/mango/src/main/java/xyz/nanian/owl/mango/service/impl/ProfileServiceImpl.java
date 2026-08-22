package xyz.nanian.owl.mango.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.mango.domain.dto.ProfileUpdateDTO;
import xyz.nanian.owl.mango.domain.entity.BlogProfileDO;
import xyz.nanian.owl.mango.domain.vo.ProfileVO;
import xyz.nanian.owl.mango.mapper.BlogProfileMapper;
import xyz.nanian.owl.mango.mapstruct.ProfileConvert;
import xyz.nanian.owl.mango.service.ProfileService;

import java.time.LocalDateTime;

/**
 * 博客站长个人信息服务实现
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final Long PROFILE_ID = 1L;

    private final BlogProfileMapper blogProfileMapper;
    private final ProfileConvert profileConvert;

    @Override
    public ProfileVO get() {
        BlogProfileDO profile = blogProfileMapper.selectById(PROFILE_ID);
        return profile == null ? null : profileConvert.toVO(profile);
    }

    @Override
    @OperationLog(module = "博客", action = "更新站长信息", persist = true)
    public Boolean update(ProfileUpdateDTO dto) {
        BlogProfileDO profile = blogProfileMapper.selectById(PROFILE_ID);
        if (profile == null) {
            return insertInitial(dto);
        }

        LambdaUpdateWrapper<BlogProfileDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(BlogProfileDO::getId, PROFILE_ID);
        setIfPresent(wrapper, dto);
        wrapper.set(BlogProfileDO::getUpdateTime, LocalDateTime.now());
        return blogProfileMapper.update(null, wrapper) > 0;
    }

    private Boolean insertInitial(ProfileUpdateDTO dto) {
        BlogProfileDO profile = new BlogProfileDO();
        profile.setId(PROFILE_ID);
        profile.setAvatarUrl(dto.getAvatarUrl());
        profile.setName(dto.getName());
        profile.setTagline(dto.getTagline());
        profile.setBio(dto.getBio());
        profile.setLocation(dto.getLocation());
        profile.setGithubUrl(dto.getGithubUrl());
        profile.setWebsiteUrl(dto.getWebsiteUrl());
        profile.setEmail(dto.getEmail());
        profile.setCodetimeUid(dto.getCodetimeUid());
        profile.setCreateTime(LocalDateTime.now());
        profile.setUpdateTime(LocalDateTime.now());
        return blogProfileMapper.insert(profile) > 0;
    }

    private void setIfPresent(LambdaUpdateWrapper<BlogProfileDO> wrapper, ProfileUpdateDTO dto) {
        if (dto.getAvatarUrl() != null) {
            wrapper.set(BlogProfileDO::getAvatarUrl, dto.getAvatarUrl());
        }
        if (dto.getName() != null) {
            wrapper.set(BlogProfileDO::getName, dto.getName());
        }
        if (dto.getTagline() != null) {
            wrapper.set(BlogProfileDO::getTagline, dto.getTagline());
        }
        if (dto.getBio() != null) {
            wrapper.set(BlogProfileDO::getBio, dto.getBio());
        }
        if (dto.getLocation() != null) {
            wrapper.set(BlogProfileDO::getLocation, dto.getLocation());
        }
        if (dto.getGithubUrl() != null) {
            wrapper.set(BlogProfileDO::getGithubUrl, dto.getGithubUrl());
        }
        if (dto.getWebsiteUrl() != null) {
            wrapper.set(BlogProfileDO::getWebsiteUrl, dto.getWebsiteUrl());
        }
        if (dto.getEmail() != null) {
            wrapper.set(BlogProfileDO::getEmail, dto.getEmail());
        }
        if (dto.getCodetimeUid() != null) {
            wrapper.set(BlogProfileDO::getCodetimeUid, dto.getCodetimeUid());
        }
    }
}
