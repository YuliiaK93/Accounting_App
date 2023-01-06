package djrAccounting.unit.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.InvoiceProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImplTest {

    private InvoiceProductServiceImpl invoiceProductService;

    @Mock
    private InvoiceProductRepository invoiceProductRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private MapperUtil mapper;

    @Mock
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        invoiceProductService = new InvoiceProductServiceImpl(invoiceProductRepository, securityService, mapper, invoiceService);
    }

    @Test
    void getTotalPriceByInvoice() {

        Invoice invoice = new Invoice() {{
            setId(1L);
        }};

        ArrayList<InvoiceProduct> invoiceProducts = new ArrayList<>() {{

            add(new InvoiceProduct() {{
                setPrice(BigDecimal.TEN);
                setQuantity(100);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(20));
                setQuantity(150);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(73.5));
                setQuantity(8);
                setTax(10);
            }});
        }};

        UserDto userDto = new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }};

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceProductRepository.findByInvoice_IdAndInvoice_Company_Id(eq(invoice.getId()), eq(userDto.getCompany().getId()))).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(4588.0), invoiceProductService.getTotalPriceByInvoice(invoice.getId()));
    }

    @Test
    void getTotalPriceWithTaxByInvoice() {

        Invoice invoice = new Invoice() {{
            setId(1L);
        }};

        ArrayList<InvoiceProduct> invoiceProducts = new ArrayList<>() {{

            add(new InvoiceProduct() {{
                setPrice(BigDecimal.TEN);
                setQuantity(100);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(20));
                setQuantity(150);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(73.5));
                setQuantity(8);
                setTax(10);
            }});
        }};

        UserDto userDto = new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }};

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceProductRepository.findByInvoice_IdAndInvoice_Company_Id(eq(invoice.getId()), eq(userDto.getCompany().getId()))).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(5047.2), invoiceProductService.getTotalPriceWithTaxByInvoice(invoice.getId()));
    }

    @Test
    void getTotalCostForCurrentCompany() {

        ArrayList<InvoiceProduct> invoiceProducts = new ArrayList<>() {{

            add(new InvoiceProduct() {{
                setPrice(BigDecimal.TEN);
                setQuantity(100);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(20));
                setQuantity(150);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(73.5));
                setQuantity(8);
                setTax(10);
            }});
        }};

        UserDto userDto = new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }};

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(eq(userDto.getCompany().getId()), eq(InvoiceType.PURCHASE))).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(5047.2), invoiceProductService.getTotalCostForCurrentCompany());
    }

    @Test
    void getTotalSalesForCurrentCompany() {

        ArrayList<InvoiceProduct> invoiceProducts = new ArrayList<>() {{

            add(new InvoiceProduct() {{
                setPrice(BigDecimal.TEN);
                setQuantity(100);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(20));
                setQuantity(150);
                setTax(10);
            }});
            add(new InvoiceProduct() {{
                setPrice(BigDecimal.valueOf(73.5));
                setQuantity(8);
                setTax(10);
            }});
        }};

        UserDto userDto = new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }};

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(eq(userDto.getCompany().getId()), eq(InvoiceType.SALES))).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(5047.2), invoiceProductService.getTotalSalesForCurrentCompany());
    }

    @Test
    void getTotalProfitLossForCurrentCompany() {

        ArrayList<InvoiceProduct> invoiceProducts = new ArrayList<>() {{

            add(new InvoiceProduct() {{
                setProfitLoss(BigDecimal.valueOf(2000));
            }});
            add(new InvoiceProduct() {{
                setProfitLoss(BigDecimal.valueOf(2500));
            }});
            add(new InvoiceProduct() {{
                setProfitLoss(BigDecimal.valueOf(547.2));
            }});
        }};

        UserDto userDto = new UserDto() {{
            setCompany(new CompanyDto() {{
                setId(1L);
            }});
        }};

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceProductRepository.findByInvoice_Company_Id(eq(userDto.getCompany().getId()))).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(5047.2), invoiceProductService.getTotalProfitLossForCurrentCompany());
    }
}