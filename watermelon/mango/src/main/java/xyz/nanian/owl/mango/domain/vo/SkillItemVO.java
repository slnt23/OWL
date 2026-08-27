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

    @Schema(description = "条目 ID")
    private Long id;

    @Schema(description = "技能名称")
    private String name;

    @Schema(description = "排序权重")
    private Integer sortOrder;
}