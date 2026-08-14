package xyz.nanian.owl.sugarcane.domain.vo;


import java.util.List;

/**
 * 分类树
 *
 * @author slnt23
 * @since 2026/4/24
 */

public class CategoryTreeVO {
    /**
     * 分类节点ID
     */
    private Long id;

    /**
     * 分类节点名称
     */
    private String name;

    /**
     * 子分类列表，为空表示该节点为叶子节点
     */
    private List<CategoryTreeVO> children;
}
