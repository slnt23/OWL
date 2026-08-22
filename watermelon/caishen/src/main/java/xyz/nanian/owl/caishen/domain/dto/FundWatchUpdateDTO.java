package xyz.nanian.owl.caishen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 更新关注备注入参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "更新关注备注入参")
public class FundWatchUpdateDTO {

    @Size(max = 200, message = "备注最大 200 字符")
    @Schema(description = "备注，如\"定投基金\"")
    private String remark;
}
