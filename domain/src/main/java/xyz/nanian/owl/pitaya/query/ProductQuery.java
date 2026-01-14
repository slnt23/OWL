package xyz.nanian.owl.pitaya.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.nanian.owl.PageQuery;

/**
 * 商品查询，通过商品名
 *
 * @author slnt23
 * @since 2025/11/10
 */

@EqualsAndHashCode(callSuper = true)
@Schema(name= "查询指定商品",description = "通过商品名查询")
@Data
public class ProductQuery extends PageQuery {

    @Schema(description = "商品名",example = "华为Pura 80")
    private String productName;

}
