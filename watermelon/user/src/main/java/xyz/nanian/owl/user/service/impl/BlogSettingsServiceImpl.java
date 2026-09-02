package xyz.nanian.owl.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nanian.owl.log.annotation.OperationLog;
import xyz.nanian.owl.user.domain.dto.BlogAboutDTO;
import xyz.nanian.owl.user.domain.dto.BlogEducationDTO;
import xyz.nanian.owl.user.domain.dto.BlogSettingsDTO;
import xyz.nanian.owl.user.domain.dto.BlogSkillCategoryDTO;
import xyz.nanian.owl.user.domain.entity.BlogEducationDO;
import xyz.nanian.owl.user.domain.entity.BlogSettingsDO;
import xyz.nanian.owl.user.domain.entity.BlogSkillCategoryDO;
import xyz.nanian.owl.user.domain.entity.BlogSkillItemDO;
import xyz.nanian.owl.user.domain.vo.BlogAboutVO;
import xyz.nanian.owl.user.domain.vo.BlogEducationVO;
import xyz.nanian.owl.user.domain.vo.BlogSettingsVO;
import xyz.nanian.owl.user.domain.vo.BlogSkillCategoryVO;
import xyz.nanian.owl.user.mapper.BlogEducationMapper;
import xyz.nanian.owl.user.mapper.BlogSettingsMapper;
import xyz.nanian.owl.user.mapper.BlogSkillCategoryMapper;
import xyz.nanian.owl.user.mapper.BlogSkillItemMapper;
import xyz.nanian.owl.user.service.BlogSettingsService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogSettingsServiceImpl implements BlogSettingsService {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final BlogSettingsMapper blogSettingsMapper;
    private final BlogEducationMapper blogEducationMapper;
    private final BlogSkillCategoryMapper blogSkillCategoryMapper;
    private final BlogSkillItemMapper blogSkillItemMapper;

    @Override
    public BlogSettingsVO get(Long userId) {
        BlogSettingsVO vo = new BlogSettingsVO();

        BlogSettingsDO settings = blogSettingsMapper.selectOne(
                Wrappers.<BlogSettingsDO>lambdaQuery().eq(BlogSettingsDO::getUserId, userId));
        if (settings != null) {
            vo.setAbout(buildAboutVO(settings));
        }

        List<BlogEducationDO> educations = blogEducationMapper.selectList(
                Wrappers.<BlogEducationDO>lambdaQuery()
                        .eq(BlogEducationDO::getUserId, userId)
                        .orderByAsc(BlogEducationDO::getSortOrder));
        vo.setEducations(educations.stream().map(e -> {
            BlogEducationVO evo = new BlogEducationVO();
            evo.setSchool(e.getSchool());
            evo.setDegree(e.getDegree());
            evo.setPeriod(e.getPeriod());
            return evo;
        }).toList());

        List<BlogSkillCategoryDO> categories = blogSkillCategoryMapper.selectList(
                Wrappers.<BlogSkillCategoryDO>lambdaQuery()
                        .eq(BlogSkillCategoryDO::getUserId, userId)
                        .orderByAsc(BlogSkillCategoryDO::getSortOrder));
        List<BlogSkillCategoryVO> skillVOs = new ArrayList<>();
        for (BlogSkillCategoryDO category : categories) {
            BlogSkillCategoryVO scvo = new BlogSkillCategoryVO();
            scvo.setCategory(category.getCategory());
            List<BlogSkillItemDO> items = blogSkillItemMapper.selectList(
                    Wrappers.<BlogSkillItemDO>lambdaQuery()
                            .eq(BlogSkillItemDO::getCategoryId, category.getId())
                            .orderByAsc(BlogSkillItemDO::getSortOrder));
            scvo.setItems(items.stream().map(BlogSkillItemDO::getItemName).toList());
            skillVOs.add(scvo);
        }
        vo.setSkills(skillVOs);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperationLog(module = "博客", action = "更新博客设置", persist = true)
    public Boolean update(BlogSettingsDTO dto, Long userId) {
        LocalDateTime now = LocalDateTime.now();

        if (dto.getAbout() != null) {
            updateAbout(dto.getAbout(), userId, now);
        }

        if (dto.getEducations() != null) {
            updateEducations(dto.getEducations(), userId, now);
        }

        if (dto.getSkills() != null) {
            updateSkills(dto.getSkills(), userId, now);
        }

        return true;
    }

    private void updateAbout(BlogAboutDTO dto, Long userId, LocalDateTime now) {
        BlogSettingsDO settings = blogSettingsMapper.selectOne(
                Wrappers.<BlogSettingsDO>lambdaQuery().eq(BlogSettingsDO::getUserId, userId));

        if (settings == null) {
            settings = new BlogSettingsDO();
            settings.setUserId(userId);
            settings.setTagLine(dto.getTagLine());
            settings.setBio(toJson(dto.getBio()));
            settings.setLocation(dto.getLocation());
            settings.setGithubUrl(dto.getGithubUrl());
            settings.setCodetimeUrl(dto.getCodetimeUrl());
            settings.setPoem(dto.getPoem());
            settings.setUpdateTime(now);
            blogSettingsMapper.insert(settings);
        } else {
            if (dto.getTagLine() != null) settings.setTagLine(dto.getTagLine());
            if (dto.getBio() != null) settings.setBio(toJson(dto.getBio()));
            if (dto.getLocation() != null) settings.setLocation(dto.getLocation());
            if (dto.getGithubUrl() != null) settings.setGithubUrl(dto.getGithubUrl());
            if (dto.getCodetimeUrl() != null) settings.setCodetimeUrl(dto.getCodetimeUrl());
            if (dto.getPoem() != null) settings.setPoem(dto.getPoem());
            settings.setUpdateTime(now);
            blogSettingsMapper.updateById(settings);
        }
    }

    private void updateEducations(List<BlogEducationDTO> dtos, Long userId, LocalDateTime now) {
        blogEducationMapper.delete(
                Wrappers.<BlogEducationDO>lambdaQuery().eq(BlogEducationDO::getUserId, userId));

        int sortOrder = 0;
        for (BlogEducationDTO dto : dtos) {
            BlogEducationDO education = new BlogEducationDO();
            education.setUserId(userId);
            education.setSchool(dto.getSchool());
            education.setDegree(dto.getDegree());
            education.setPeriod(dto.getPeriod());
            education.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : sortOrder++);
            education.setCreateTime(now);
            blogEducationMapper.insert(education);
        }
    }

    private void updateSkills(List<BlogSkillCategoryDTO> dtos, Long userId, LocalDateTime now) {
        List<BlogSkillCategoryDO> existingCategories = blogSkillCategoryMapper.selectList(
                Wrappers.<BlogSkillCategoryDO>lambdaQuery()
                        .eq(BlogSkillCategoryDO::getUserId, userId));
        List<Long> existingIds = existingCategories.stream()
                .map(BlogSkillCategoryDO::getId).collect(Collectors.toList());
        if (!existingIds.isEmpty()) {
            blogSkillItemMapper.delete(
                    Wrappers.<BlogSkillItemDO>lambdaQuery().in(BlogSkillItemDO::getCategoryId, existingIds));
            blogSkillCategoryMapper.delete(
                    Wrappers.<BlogSkillCategoryDO>lambdaQuery().eq(BlogSkillCategoryDO::getUserId, userId));
        }

        int sortOrder = 0;
        for (BlogSkillCategoryDTO dto : dtos) {
            BlogSkillCategoryDO category = new BlogSkillCategoryDO();
            category.setUserId(userId);
            category.setCategory(dto.getCategory());
            category.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : sortOrder++);
            category.setCreateTime(now);
            blogSkillCategoryMapper.insert(category);

            if (dto.getItems() != null && !dto.getItems().isEmpty()) {
                int itemSort = 0;
                for (String itemName : dto.getItems()) {
                    BlogSkillItemDO item = new BlogSkillItemDO();
                    item.setCategoryId(category.getId());
                    item.setItemName(itemName);
                    item.setSortOrder(itemSort++);
                    item.setCreateTime(now);
                    blogSkillItemMapper.insert(item);
                }
            }
        }
    }

    private BlogAboutVO buildAboutVO(BlogSettingsDO settings) {
        BlogAboutVO vo = new BlogAboutVO();
        vo.setTagLine(settings.getTagLine());
        vo.setBio(parseBio(settings.getBio()));
        vo.setLocation(settings.getLocation());
        vo.setGithubUrl(settings.getGithubUrl());
        vo.setCodetimeUrl(settings.getCodetimeUrl());
        vo.setPoem(settings.getPoem());
        return vo;
    }

    private List<String> parseBio(String bioJson) {
        if (bioJson == null || bioJson.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return OBJECT_MAPPER.readValue(bioJson, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            return Collections.emptyList();
        }
    }

    private String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}