package xyz.nanian.owl.pitaya.dto;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * 订单DTO
 *
 * @author slnt23
 * @since 2025/11/10
 */

@Schema(name = "订单DTO")
public class OrderDTO {

    @Schema(description = "订单编号",example = "UUID")
    String orderCode;

    @Schema(description = "订单状态", example = "1",
            allowableValues = {"1", "2", "3", "4"})
    private String status;

    @Schema(description = "下单时间", example = "2025-11-23T12:12:12")
    private LocalDateTime orderTime;

    @Schema(description = "发货时间", example = "2025-11-24T09:30:00")
    private LocalDateTime shipTime;

    @Schema(description = "取消时间", example = "2025-11-23T15:45:00")
    private LocalDateTime cancelTime;

    @Schema(description = "完成时间", example = "2025-11-25T18:00:00")
    private LocalDateTime finishTime;


}
