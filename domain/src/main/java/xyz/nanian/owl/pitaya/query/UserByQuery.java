package xyz.nanian.owl.pitaya.query;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户查询
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Data
@Schema(name = "查询指定用户",description = "通过用户名查询")
public class UserByQuery {

    @Schema(description = "用户名",example = "秦明")
    private String userName;

}
