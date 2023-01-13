package djrAccounting.service.implementation;

import djrAccounting.client.ExchangeClient;
import djrAccounting.dto.dashboard.FinancialSummaryDto;
import djrAccounting.service.InvoiceProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTestMentor {
    @Mock
    private InvoiceProductService invoiceProductService;

    @Mock
    private ExchangeClient exchangeClient;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Test
    void financialSummaryForCurrentCompany() {
        BigDecimal totalCost = BigDecimal.TEN;
        BigDecimal totalSales = BigDecimal.ONE;
        BigDecimal profitLoss = BigDecimal.ZERO;

        when(invoiceProductService.getTotalCostForCurrentCompany()).thenReturn(totalCost);
        when(invoiceProductService.getTotalSalesForCurrentCompany()).thenReturn(totalSales);
        when(invoiceProductService.getTotalProfitLossForCurrentCompany()).thenReturn(profitLoss);

        FinancialSummaryDto financialSummaryDto = dashboardService.financialSummaryForCurrentCompany();

        assertEquals(totalCost, financialSummaryDto.getTotalCost());
        assertEquals(totalSales, financialSummaryDto.getTotalSales());
        assertEquals(profitLoss, financialSummaryDto.getProfitLoss());
    }
}