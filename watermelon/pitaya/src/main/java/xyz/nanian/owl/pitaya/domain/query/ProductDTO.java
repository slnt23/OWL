package xyz.nanian.owl.pitaya.domain.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.dto.PageDTO;

/**
 * 商品查询，通过商品名
 *
 * @author slnt23
 * @since 2025/11/10
 */

@EqualsAndHashCode(callSuper = true)
@Schema(name= "商品查询",description = "通过商品名查询")
@Data
public class ProductDTO extends PageDTO {

    @Schema(description = "商品名",example = "华为Pura 80")
    private String productName;

}
