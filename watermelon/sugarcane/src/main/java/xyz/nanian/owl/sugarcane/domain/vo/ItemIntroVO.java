package xyz.nanian.owl.sugarcane.domain.vo;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 物品简介列表VO
 *
 * @author slnt23
 * @since 2026/4/26
 */

@Data
public class ItemIntroVO {

    /**
     * id
     */
    Long id;

    /**
     * 单位
     */
    String unit;

    /**
     * 分类id
     */
    Long categoryId;

    @NotNull
    String itemName;
//    List<String> ItemIntroList;
}
