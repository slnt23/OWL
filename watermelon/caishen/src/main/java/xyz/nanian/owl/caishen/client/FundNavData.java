package xyz.nanian.owl.caishen.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 行情客户端返回的净值数据。
 *
 * @author slnt23
 * @since 2026/8/23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundNavData {

    private String fundCode;

    private LocalDate navDate;

    private BigDecimal unitNav;

    private BigDecimal accumulatedNav;

    private BigDecimal dailyReturnRate;
}
