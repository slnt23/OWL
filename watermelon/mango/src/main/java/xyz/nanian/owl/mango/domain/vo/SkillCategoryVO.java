package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 技能分类 VO（含条目列表）
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "技能分类 VO")
public class SkillCategoryVO {

    @Schema(description = "分类 ID")
    private Long id;

    @Schema(description = "分类名称")
    private String category;

    @Schema(description = "技能条目列表")
    private List<SkillItemVO> items;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}