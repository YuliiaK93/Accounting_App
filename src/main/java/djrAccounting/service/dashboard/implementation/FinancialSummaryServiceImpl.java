package djrAccounting.service.dashboard.implementation;

import djrAccounting.dto.dashboard.FinancialSummaryDto;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.dashboard.FinancialSummaryService;
import org.springframework.stereotype.Service;

@Service
public class FinancialSummaryServiceImpl implements FinancialSummaryService {

    private final InvoiceProductService invoiceProductService;

    public FinancialSummaryServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public FinancialSummaryDto financialSummaryForCurrentCompany() {

        return FinancialSummaryDto.builder()
                .totalSales(invoiceProductService.getTotalSalesForCurrentCompany())
                .totalCost(invoiceProductService.getTotalCostForCurrentCompany())
                .profitLoss(invoiceProductService.getTotalSalesForCurrentCompany()
                        .subtract(invoiceProductService.getTotalCostForCurrentCompany())).build();
    }
}
