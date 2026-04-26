package xyz.nanian.owl.sugarcane.domain.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.dto.PageDTO;

/**
 * 物品简介DTO
 *
 * @author slnt23
 * @since 2026/4/26
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemIntroDTO extends PageDTO {

    /**
     * 所查物品名
     */
    @NotNull
    String ItemName;

}
