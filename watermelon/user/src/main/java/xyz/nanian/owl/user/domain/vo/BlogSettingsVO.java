package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "博客设置 VO")
public class BlogSettingsVO {

    @Schema(description = "About 信息")
    private BlogAboutVO about;

    @Schema(description = "教育经历列表")
    private List<BlogEducationVO> educations;

    @Schema(description = "技能分类列表")
    private List<BlogSkillCategoryVO> skills;
}