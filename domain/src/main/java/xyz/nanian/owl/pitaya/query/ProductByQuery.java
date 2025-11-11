package xyz.nanian.owl.pitaya.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品查询
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name= "查询指定商品",description = "通过商品名查询")
@Data
public class ProductByQuery {

    @Schema(description = "商品名",example = "火龙果")
    private String productName;


}
