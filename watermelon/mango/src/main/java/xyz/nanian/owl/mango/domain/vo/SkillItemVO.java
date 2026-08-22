package xyz.nanian.owl.mango.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 技能条目 VO
 *
 * @author slnt23
 * @since 2026/8/22
 */
@Data
@Schema(name = "技能条目 VO")
public class SkillItemVO {

    /**
     * 条目 ID
     */
    @Schema(description = "条目 ID")
    private Long id;

    /**
     * 技能名称
     */
    @Schema(description = "技能名称")
    private String name;

    /**
     * 排序权重
     */
    @Schema(description = "排序权重")
    private Integer sortOrder;
}
