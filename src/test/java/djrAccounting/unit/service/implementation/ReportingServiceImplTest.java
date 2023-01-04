package djrAccounting.unit.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.dto.ProductDto;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.implementation.ReportingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportingServiceImplTest {

    private ReportingServiceImpl reportingService;

    @Mock
    private InvoiceProductService invoiceProductService;

    @BeforeEach
    void setUp() {
        reportingService = new ReportingServiceImpl(invoiceProductService);
    }

    @Test
    void getAllInvoiceProductsThatApprovedFroCurrentCompany() {

        List<InvoiceProductDto> invoiceProductDTOs = new ArrayList<>() {{

            add(new InvoiceProductDto(1L, 5, BigDecimal.TEN, 9, BigDecimal.ONE, 0, new InvoiceDto(), new ProductDto(), BigDecimal.valueOf(10.9)));
            add(new InvoiceProductDto());
        }};

        when(invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany()).thenReturn(invoiceProductDTOs);

        assertEquals(invoiceProductDTOs, reportingService.getAllInvoiceProductsThatApprovedFroCurrentCompany());
    }

    @Test
    void getAllProfitLossPerMonth() {

        List<InvoiceProductDto> invoiceProductDTOs = new ArrayList<>() {{

            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now());}});
                setProfitLoss(BigDecimal.TEN);
            }});

            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now());}});
                setProfitLoss(BigDecimal.valueOf(20));
            }});

            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now());}});
                setProfitLoss(BigDecimal.valueOf(30));
            }});
            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now().minusMonths(1));}});
                setProfitLoss(BigDecimal.TEN);
            }});

            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now().minusMonths(1));}});
                setProfitLoss(BigDecimal.valueOf(15));
            }});

            add(new InvoiceProductDto(){{
                setInvoice(new InvoiceDto(){{setDate(LocalDate.now().minusMonths(1));}});
                setProfitLoss(BigDecimal.valueOf(25));
            }});
        }};

        when(invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany()).thenReturn(invoiceProductDTOs);

        Map<String, BigDecimal> profitLossPerMonth = reportingService.getAllProfitLossPerMonth();

        assertEquals(BigDecimal.valueOf(60), profitLossPerMonth.get(LocalDate.now().getYear() + " " + LocalDate.now().getMonth()));
        assertEquals(BigDecimal.valueOf(50), profitLossPerMonth.get(LocalDate.now().minusMonths(1).getYear() + " " + LocalDate.now().minusMonths(1).getMonth()));
    }
}