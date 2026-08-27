package xyz.nanian.owl.sugarcane.domain.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 分类树
 *
 * @author slnt23
 * @since 2026/4/24
 */

@Schema(name = "分类树VO")
public class CategoryTreeVO {
    @Schema(description = "分类节点ID")
    private Long id;

    @Schema(description = "分类节点名称")
    private String name;

    @Schema(description = "子分类列表，为空表示该节点为叶子节点")
    private List<CategoryTreeVO> children;
}