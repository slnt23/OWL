package xyz.nanian.owl.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "博客设置更新入参")
public class BlogSettingsDTO {

    @Schema(description = "About 信息")
    private BlogAboutDTO about;

    @Schema(description = "教育经历列表")
    private List<BlogEducationDTO> educations;

    @Schema(description = "技能分类列表")
    private List<BlogSkillCategoryDTO> skills;
}