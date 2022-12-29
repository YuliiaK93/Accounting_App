package djrAccounting.dto.dashboard;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FinancialSummaryDto {

    private BigDecimal totalCost;
    private BigDecimal totalSales;
    private BigDecimal profitLoss;
}