package djrAccounting.unit.service.implementation;

import djrAccounting.client.ExchangeClient;
import djrAccounting.dto.dashboard.FinancialSummaryDto;
import djrAccounting.service.DashboardService;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.implementation.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    private DashboardService dashboardService;

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


        when(invoiceProductService.getTotalSalesForCurrentCompany());

        FinancialSummaryDto financialSummaryDto = dashboardService.financialSummaryForCurrentCompany();


    }
}