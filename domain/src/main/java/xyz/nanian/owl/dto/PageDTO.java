package xyz.nanian.owl.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 分页查询相关参数
 *
 * @author slnt23
 * @since 2025/11/11
 */

@Getter
@Setter
@ToString
public class PageDTO {

    @Min(value = 1,message = "页数最小值是1")
    @Schema(description = "查询页数",example = "1")
    private Integer pageNum= 1;

    @Min(value = 1,message = "条数最小值是1")
    @Schema(description = "查询条数",example = "10")
    private Integer pageSize = 10;
}
