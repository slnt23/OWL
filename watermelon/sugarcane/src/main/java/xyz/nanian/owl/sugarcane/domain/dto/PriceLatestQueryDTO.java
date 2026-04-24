package xyz.nanian.owl.sugarcane.domain.dto;


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
public class PriceLatestQueryDTO extends BasePriceQueryDTO {

    // 是否只取每个维度最新一条
    private Boolean latestOnly = true;

}
