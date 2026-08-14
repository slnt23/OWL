package xyz.nanian.owl.admin.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * featureDTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class FeatureDTO {
    /** 新增时不传，修改时必传 */
    private Long id;

    /** 图标标识：示例 '01'、'🔥'、'star' */
    @NotBlank(message = "图标不能为空")
    private String icon;

    /** 标题：建议6字以内 */
    @NotBlank(message = "标题不能为空")
    private String title;

    /** 描述文案：建议50字以内 */
    private String description;

    /** 排序序号：数值越小越靠前 */
    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;
}
