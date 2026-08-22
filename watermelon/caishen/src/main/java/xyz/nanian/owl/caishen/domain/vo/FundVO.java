package xyz.nanian.owl.caishen.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 基金档案出参。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Schema(name = "基金档案出参")
public class FundVO {

    @Schema(description = "基金档案 ID")
    private Long id;

    @Schema(description = "基金代码")
    private String fundCode;

    @Schema(description = "基金名称")
    private String fundName;

    @Schema(description = "基金类型：股票型/混合型/债券型/货币型/指数型")
    private String fundType;

    @Schema(description = "状态：1=正常，0=停用")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
