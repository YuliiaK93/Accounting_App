package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.exception.InvoiceProductNotFoundException;
import djrAccounting.repository.InvoiceProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceProductServiceImplTest {

    @Mock
    private InvoiceProductRepository invoiceProductRepository;
    @InjectMocks
    private InvoiceProductServiceImpl invoiceProductService;

    @Test
    @DisplayName("When invoice product is deleted, it is soft-deleted from the database")
    void deleteInvoiceProductById_test() {
        //given
        InvoiceProduct invoiceProduct = InvoiceProduct.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).build();
        //when
        when(invoiceProductRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.of(invoiceProduct));
        invoiceProductService.deleteInvoiceProductById(invoiceProduct.getId());
        //then
        assertTrue(invoiceProduct.getIsDeleted());
    }
    @Test
    @DisplayName("When invoice product is deleted, it is hard-deleted from the database")
    void deleteInvoiceProductById_test_throws(){
        when(invoiceProductRepository.findById(TestConstants.SAMPLE_ID1)).thenReturn(Optional.empty());
        assertThrows(InvoiceProductNotFoundException.class,()->invoiceProductService.deleteInvoiceProductById(TestConstants.SAMPLE_ID1));
    }
}