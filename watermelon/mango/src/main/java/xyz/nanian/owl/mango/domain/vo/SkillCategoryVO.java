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

    /**
     * 分类 ID
     */
    @Schema(description = "分类 ID")
    private Long id;

    /**
     * 分类名称
     */
    @Schema(description = "分类名称")
    private String category;

    /**
     * 技能条目列表
     */
    @Schema(description = "技能条目列表")
    private List<SkillItemVO> items;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
