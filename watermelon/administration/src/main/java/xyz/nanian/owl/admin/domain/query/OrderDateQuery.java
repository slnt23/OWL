package xyz.nanian.owl.admin.domain.query;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

/**
 * 统计指定日期内的订单数量
 *
 * @author slnt23
 * @since 2025/11/23
 */

@Schema(name = "订单数量查询")
public class OrderDateQuery {

    @Schema(description = "下单时间开始日期",example = "2025-10-10")
    private LocalDate orderDateStart;

    @Schema(description = "下单时间结束日期",example = "2025-11-11")
    private LocalDate OrderDateEnd;
}
