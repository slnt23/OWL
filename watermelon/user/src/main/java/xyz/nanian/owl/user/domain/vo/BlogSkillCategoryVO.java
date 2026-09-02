package xyz.nanian.owl.user.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(name = "博客技能分类 VO")
public class BlogSkillCategoryVO {

    @Schema(description = "分类名称")
    private String category;

    @Schema(description = "技能项列表")
    private List<String> items;
}