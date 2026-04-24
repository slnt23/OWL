package xyz.nanian.owl.sugarcane.domain.vo;


import java.util.List;

/**
 * 分类树
 *
 * @author slnt23
 * @since 2026/4/24
 */

public class CategoryTreeVO {
    Long id;
    String name;
    List<CategoryTreeVO> children;
}
