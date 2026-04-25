package xyz.nanian.owl.admin.domain.vo;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 焦点展示VO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Data
public class SpotlightVO {

    /** 新增时不传，修改时必传 */
    private Integer id;

    @NotBlank(message = "眉题不能为空")
    private String eyebrow;

    @NotBlank(message = "主标题不能为空")
    private String title;

    private String description;

    @NotBlank(message = "图片不能为空")
    private String imageUrl;

    @NotNull(message = "排序序号不能为空")
    private Integer order;

    private String link;

    private String target;

}
