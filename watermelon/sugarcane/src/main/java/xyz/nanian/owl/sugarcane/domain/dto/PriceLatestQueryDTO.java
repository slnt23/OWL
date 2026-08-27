package xyz.nanian.owl.sugarcane.domain.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 最新价格查询DTO
 *
 * @author slnt23
 * @since 2026/4/24
 */

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(name = "最新价格查询DTO")
public class PriceLatestQueryDTO extends BasePriceQueryDTO {
}