package djrAccounting.unit.service.implementation;

import djrAccounting.client.ExchangeClient;
import djrAccounting.dto.dashboard.FinancialSummaryDto;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.implementation.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    private DashboardServiceImpl dashboardService;

    @Mock
    private InvoiceProductService invoiceProductService;

    @Mock
    private ExchangeClient exchangeClient;

    @BeforeEach
    void setUp() {
        dashboardService = new DashboardServiceImpl(invoiceProductService, exchangeClient);
    }

    @Test
    void financialSummaryForCurrentCompany() {

        when(invoiceProductService.getTotalCostForCurrentCompany()).thenReturn(BigDecimal.TEN);
        when(invoiceProductService.getTotalSalesForCurrentCompany()).thenReturn(BigDecimal.ONE);
        when(invoiceProductService.getTotalProfitLossForCurrentCompany()).thenReturn(BigDecimal.ZERO);

        FinancialSummaryDto financialSummaryDto = dashboardService.financialSummaryForCurrentCompany();

        assertEquals(BigDecimal.TEN, financialSummaryDto.getTotalCost());
        assertEquals(BigDecimal.ONE, financialSummaryDto.getTotalSales());
        assertEquals(BigDecimal.ZERO, financialSummaryDto.getProfitLoss());
    }

    @Test
    void verify_financialSummaryForCurrentCompany_returns_FinancialSummaryDto() {
        assertTrue(dashboardService.financialSummaryForCurrentCompany() instanceof FinancialSummaryDto);
    }

    @Test
    void verify_financialSummaryForCurrentCompany_consumes_InvoiceProductService() {
        dashboardService.financialSummaryForCurrentCompany();

        InOrder inOrder = inOrder(invoiceProductService);
        inOrder.verify(invoiceProductService).getTotalSalesForCurrentCompany();
        inOrder.verify(invoiceProductService).getTotalCostForCurrentCompany();
        inOrder.verify(invoiceProductService).getTotalProfitLossForCurrentCompany();
    }
}