package djrAccounting.unit.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Invoice;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.InvoiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    private InvoiceServiceImpl invoiceService;

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceProductService invoiceProductService;
    @Mock
    private SecurityService securityService;
    @Mock
    private MapperUtil mapper;


    @BeforeEach
    void setUp() {

        invoiceService = new InvoiceServiceImpl(invoiceRepository, invoiceProductService, securityService, mapper);
    }

    @Test
    void getLast3ApprovedInvoicesForCurrentUserCompany() {

        List<InvoiceDto> invoiceDTOs = new ArrayList<>() {{

            add(new InvoiceDto());
            add(new InvoiceDto());
            add(new InvoiceDto());
        }};

        List<Invoice> repoInvoices = new ArrayList<>() {{

            add(new Invoice());
            add(new Invoice());
            add(new Invoice());
        }};

        when(securityService.getLoggedInUser()).thenReturn(new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }});
        when(invoiceRepository.getLast3ApprovedInvoicesByCompanyId(any(Long.class))).thenReturn(repoInvoices);
        when(mapper.convert(any(Invoice.class), eq(InvoiceDto.class))).thenReturn(invoiceDTOs.get(0), invoiceDTOs.get(1), invoiceDTOs.get(2));
        when(invoiceProductService.getTotalPriceByInvoice(any())).thenReturn(BigDecimal.TEN);
        when(invoiceProductService.getTotalPriceWithTaxByInvoice(any())).thenReturn(BigDecimal.valueOf(11), BigDecimal.valueOf(12), BigDecimal.valueOf(13));

        List<InvoiceDto> invoices = invoiceService.getLast3ApprovedInvoicesForCurrentUserCompany();

        assertEquals(BigDecimal.ONE, invoices.get(0).getTax());
        assertEquals(BigDecimal.valueOf(2), invoices.get(1).getTax());
        assertEquals(BigDecimal.valueOf(3), invoices.get(2).getTax());
    }
}