package djrAccounting.service.implementation;

import djrAccounting.TestConstants;
import djrAccounting.entity.ClientVendor;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientVendorServiceImplTestMentor {

    @Mock
    ClientVendorRepository clientVendorRepository;

    @Mock
    MapperUtil mapperUtil;

    @Mock
    SecurityService securityService;

    @Mock
    InvoiceService invoiceService;

    @InjectMocks
    ClientVendorServiceImpl clientVendorServiceImpl;

    @Test
    @DisplayName("When a client/vendor has invoice, then delete method should throw IllegalAccessException")
    void deleteById_client_has_invoice_should_throw() {
        ClientVendor clientVendor = ClientVendor.builder().id(TestConstants.SAMPLE_ID1).isDeleted(false).build();

        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(clientVendor));
        when(invoiceService.existsByClientVendorId(anyLong())).thenReturn(true);

        assertThrows(IllegalAccessException.class, () -> clientVendorServiceImpl.deleteById(1L));

        verify(clientVendorRepository).findById(1L);
        verify(invoiceService).existsByClientVendorId(1L);
    }

    @Test
    public void deleteById_ClientVendorNotFound_Test() {
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> clientVendorServiceImpl.deleteById(1L));
    }
}