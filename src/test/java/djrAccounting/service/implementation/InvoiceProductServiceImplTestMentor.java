package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.RoleEnum;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImplTestMentor {
    @Mock
    private InvoiceProductRepository invoiceProductRepository;

    @Mock
    private SecurityService securityService;

    @Mock
    private MapperUtil mapper;

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    @Test
    @Description("When a list of InvoiceProduct is provided, then the summation of the profitLoss is expected")
    void getTotalProfitLossForCurrentCompany() {
        List<InvoiceProduct> invoiceProducts = List.of(
                InvoiceProduct.builder().profitLoss(BigDecimal.valueOf(2000)).build(),
                InvoiceProduct.builder().profitLoss(BigDecimal.valueOf(2500)).build(),
                InvoiceProduct.builder().profitLoss(BigDecimal.valueOf(547.2)).build());

        when(securityService.getLoggedInUser()).thenReturn(TestConstants.getTestUserDto(RoleEnum.ADMIN));
        when(invoiceProductRepository.findByInvoice_Company_Id(anyLong())).thenReturn(invoiceProducts);

        assertEquals(BigDecimal.valueOf(5047.2), invoiceProductService.getTotalProfitLossForCurrentCompany());
    }
}