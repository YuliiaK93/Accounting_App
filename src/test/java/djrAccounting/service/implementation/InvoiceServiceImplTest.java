package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.*;
import djrAccounting.enums.CompanyStatus;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.exception.InvoiceNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.ProductService;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @Mock
    private ProductService productService;
    @Mock
    private SecurityService securityService;
    @InjectMocks
    private InvoiceServiceImpl invoiceService;
    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("When invoice product id is not found, it throws InvoiceProductNotFoundException")
    void approveInvoiceById_throws_invoiceNotFoundException() {
        when(invoiceRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.empty());
        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.approveInvoiceById(TestConstants.SAMPLE_ID1));
    }

    @Test
    @DisplayName("Calculates the profit loss correctly, setStatus as APPROVED ")
    void approveInvoiceById_test() {
        Invoice salesInvoice = Invoice.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).invoiceNo(TestConstants.SAMPLE_SALES_INVOICE_NO1)
                .invoiceStatus(InvoiceStatus.AWAITING_APPROVAL).invoiceType(InvoiceType.SALES).build();
        Invoice purchaseInvoice = Invoice.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).invoiceNo(TestConstants.SAMPLE_PURCHASE_INVOICE_NO1)
                .invoiceStatus(InvoiceStatus.APPROVED).invoiceType(InvoiceType.PURCHASE).build();

        Product product1 = Product.builder().name(TestConstants.SAMPLE_PRODUCT1).quantityInStock(10).id(1L).build();
        Product product2 = Product.builder().name(TestConstants.SAMPLE_PRODUCT2).quantityInStock(10).id(2L).build();
        InvoiceProduct salesInvoiceProduct1 = InvoiceProduct.builder().profitLoss(BigDecimal.ZERO).quantity(4).price(BigDecimal.valueOf(1000)).tax(10).product(product1).build();
        InvoiceProduct salesInvoiceProduct2 = InvoiceProduct.builder().profitLoss(BigDecimal.ZERO).quantity(2).price(BigDecimal.valueOf(500)).tax(10).product(product2).build();
        salesInvoice.setInvoiceProducts(Arrays.asList(salesInvoiceProduct1, salesInvoiceProduct2));
        InvoiceProduct purchaseInvoiceProduct1 = InvoiceProduct.builder().product(product1).profitLoss(BigDecimal.ZERO).remainingQuantity(6).price(BigDecimal.valueOf(500)).tax(10).build();
        InvoiceProduct purchaseInvoiceProduct2 = InvoiceProduct.builder().product(product2).profitLoss(BigDecimal.ZERO).remainingQuantity(6).price(BigDecimal.valueOf(600)).tax(10).build();
        purchaseInvoice.setInvoiceProducts(Arrays.asList(purchaseInvoiceProduct1, purchaseInvoiceProduct2));

        when(invoiceRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.of(salesInvoice));
        when(invoiceProductRepository.findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(product1.getId())).thenReturn(List.of(purchaseInvoiceProduct1));
        when(invoiceProductRepository.findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(product2.getId())).thenReturn(List.of(purchaseInvoiceProduct2));
        product1.setQuantityInStock(product1.getQuantityInStock() - salesInvoiceProduct1.getQuantity());
        product2.setQuantityInStock(product2.getQuantityInStock() - salesInvoiceProduct2.getQuantity());
        invoiceService.approveInvoiceById(TestConstants.SAMPLE_ID1);

        assertEquals(InvoiceStatus.APPROVED, salesInvoice.getInvoiceStatus());
        assertEquals(BigDecimal.valueOf(2200), salesInvoiceProduct1.getProfitLoss());
        assertEquals(BigDecimal.valueOf(-220), salesInvoiceProduct2.getProfitLoss());
        assertEquals(2, purchaseInvoiceProduct1.getRemainingQuantity());
        assertEquals(4, purchaseInvoiceProduct2.getRemainingQuantity());
        assertEquals(6, product1.getQuantityInStock());
        assertEquals(8, product2.getQuantityInStock());
    }

    @Test
    @DisplayName("When the company has no invoice, its first sales InvoiceNo should be S-001")
    void nextSalesInvoiceNo_firstInvoice_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);

        assertEquals("S-001", invoiceService.nextSalesInvoiceNo());
    }

    @Test
    @DisplayName("The next sales invoice no must be in same format with the consecutive number (1 digit)")
    void nextSalesInvoiceNo_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();
        Invoice invoice = Invoice.builder().invoiceNo(TestConstants.SAMPLE_SALES_INVOICE_NO1).build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(1L, InvoiceType.SALES)).thenReturn(invoice);

        assertEquals("S-002", invoiceService.nextSalesInvoiceNo());
    }

    @Test
    @DisplayName("The next sales invoice no must be in same format with the consecutive number (2 digit)")
    void nextSalesInvoiceNo_2digit_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();
        Invoice invoice = Invoice.builder().invoiceNo("S-099").build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(1L, InvoiceType.SALES)).thenReturn(invoice);

        assertEquals("S-100", invoiceService.nextSalesInvoiceNo());
    }

    @Test
    @DisplayName("When the company has no invoice, its first purchase InvoiceNo should be P-001")
    void nextPurchaseInvoiceNo_firstInvoice_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);

        assertEquals("P-001", invoiceService.nextPurchaseInvoiceNo());
    }

    @Test
    @DisplayName("The next purchase invoice no must be in same format with the consecutive number (1 digit)")
    void nextPurchaseInvoiceNo_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();
        Invoice invoice = Invoice.builder().invoiceNo("P-007").build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(1L, InvoiceType.PURCHASE)).thenReturn(invoice);

        assertEquals("P-008", invoiceService.nextPurchaseInvoiceNo());
    }

    @Test
    @DisplayName("The next purchase invoice no must be in same format with the consecutive number (2 digit)")
    void nextPurchaseInvoiceNo_2digit_test() {
        CompanyDto companyDto = CompanyDto.builder().id(1L).companyStatus(CompanyStatus.ACTIVE).build();
        UserDto userDto = UserDto.builder().id(1L).company(companyDto).build();
        Invoice invoice = Invoice.builder().invoiceNo("P-077").build();

        when(securityService.getLoggedInUser()).thenReturn(userDto);
        when(invoiceRepository.findTopByCompanyIdAndInvoiceTypeOrderByIdDesc(1L, InvoiceType.PURCHASE)).thenReturn(invoice);

        assertEquals("P-078", invoiceService.nextPurchaseInvoiceNo());
    }

}