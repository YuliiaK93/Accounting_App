package djrAccounting.service.implementation;

import djrAccounting.client.ExchangeClient;
import djrAccounting.dto.currency.UsdDto;
import djrAccounting.dto.dashboard.FinancialSummaryDto;
import djrAccounting.service.DashboardService;
import djrAccounting.service.InvoiceProductService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final ExchangeClient exchangeClient;
    private final InvoiceProductService invoiceProductService;

    public DashboardServiceImpl(InvoiceProductService invoiceProductService, ExchangeClient exchangeClient) {
        this.invoiceProductService = invoiceProductService;
        this.exchangeClient = exchangeClient;
    }

    @Override
    public FinancialSummaryDto financialSummaryForCurrentCompany() {

        return FinancialSummaryDto.builder()
                .totalSales(invoiceProductService.getTotalSalesForCurrentCompany())
                .totalCost(invoiceProductService.getTotalCostForCurrentCompany())
                .profitLoss(invoiceProductService.getTotalSalesForCurrentCompany()
                        .subtract(invoiceProductService.getTotalCostForCurrentCompany())).build();
    }

    @Override
    public UsdDto getExchangeRates() {
        return exchangeClient.getExchangeRates().getUsdDto();
    }
}
