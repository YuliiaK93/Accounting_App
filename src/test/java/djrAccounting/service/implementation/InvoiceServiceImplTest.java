package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.bootstrap.StaticConstants;
import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.entity.Product;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.exception.InvoiceNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.ProductService;
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
    @InjectMocks
    private InvoiceServiceImpl invoiceService;
    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    @Test
    @DisplayName("When invoice product id is not found, it throws InvoiceProductNotFoundException")
    void approveInvoiceById_throws_invoiceNotFoundException() {
        when(invoiceRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.empty());
        assertThrows(InvoiceNotFoundException.class,()->invoiceService.approveInvoiceById(TestConstants.SAMPLE_ID1));
    }
    @Test
    @DisplayName("Calculates the quantity in stock,remaining quantity and profit loss correctly ")
    void approveInvoiceById_test() {
        InvoiceDto invoiceDto = TestConstants.getTestInvoiceDto(InvoiceStatus.AWAITING_APPROVAL,InvoiceType.SALES);
        Invoice invoice = Invoice.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).invoiceNo(TestConstants.SAMPLE_SALES_INVOICE_NO1)
                .invoiceStatus(InvoiceStatus.AWAITING_APPROVAL).invoiceType(InvoiceType.SALES).build();

        Product product1 = Product.builder().name(TestConstants.SAMPLE_PRODUCT1).quantityInStock(10).id(1L).build();
        Product product2 = Product.builder().name(TestConstants.SAMPLE_PRODUCT2).quantityInStock(10).id(2L).build();
        InvoiceProduct salesInvoiceProduct1= InvoiceProduct.builder().profitLoss(BigDecimal.ZERO).quantity(4).price(BigDecimal.valueOf(1000)).tax(10).product(product1).build();
        InvoiceProduct salesInvoiceProduct2= InvoiceProduct.builder().profitLoss(BigDecimal.ZERO).quantity(2).price(BigDecimal.valueOf(500)).tax(10).product(product2).build();
        invoice.setInvoiceProducts(Arrays.asList(salesInvoiceProduct1,salesInvoiceProduct2));
        InvoiceProduct purchaseInvoiceProduct1= InvoiceProduct.builder().product(product1).profitLoss(BigDecimal.ZERO).remainingQuantity(6).price(BigDecimal.valueOf(500)).tax(10).build();
        InvoiceProduct purchaseInvoiceProduct2= InvoiceProduct.builder().product(product2).profitLoss(BigDecimal.ZERO).remainingQuantity(6).price(BigDecimal.valueOf(600)).tax(10).build();

        when(invoiceRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.of(invoice));
        when(invoiceProductRepository.findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(product1.getId())).thenReturn(List.of(purchaseInvoiceProduct1));
        when(invoiceProductRepository.findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(product2.getId())).thenReturn(List.of(purchaseInvoiceProduct2));
        productService.decreaseQuantityInStock(product1.getId(),salesInvoiceProduct1.getQuantity());
        productService.decreaseQuantityInStock(product2.getId(),salesInvoiceProduct2.getQuantity());
        invoiceService.approveInvoiceById(TestConstants.SAMPLE_ID1);

        assertEquals(6,product1.getQuantityInStock());
        assertEquals(8,product1.getQuantityInStock());
        assertEquals(2,purchaseInvoiceProduct1.getRemainingQuantity());
        assertEquals(4,purchaseInvoiceProduct1.getRemainingQuantity());
        assertEquals(BigDecimal.valueOf(2200),salesInvoiceProduct1.getProfitLoss());
        assertEquals(BigDecimal.valueOf(-220),salesInvoiceProduct1.getProfitLoss());


    }

}