package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.common.dto.PageDTO;

/**
 * 物品简介DTO
 *
 * @author slnt23
 * @since 2026/4/26
 */

@EqualsAndHashCode(callSuper = true)
@Data
public class ItemIntroDTO extends PageDTO {

    @Schema(description = "所查物品名", example = "白砂糖")
    @NotNull
    String itemName;

}
