package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 添加关注基金入参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "添加关注基金入参")
public class FundWatchCreateDTO {

    @NotBlank(message = "基金代码不能为空")
    @Size(max = 20, message = "基金代码最大 20 字符")
    @Schema(description = "基金代码")
    private String fundCode;

    @Size(max = 200, message = "备注最大 200 字符")
    @Schema(description = "备注，如\"定投基金\"")
    private String remark;
}
