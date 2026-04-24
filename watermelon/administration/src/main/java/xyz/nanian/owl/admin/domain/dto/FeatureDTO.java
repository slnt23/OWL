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
    private Integer id;

    @NotBlank(message = "图标不能为空")
    private String icon;

    @NotBlank(message = "标题不能为空")
    private String title;

    private String description;

    @NotNull(message = "排序序号不能为空")
    private Integer sortOrder;
}
