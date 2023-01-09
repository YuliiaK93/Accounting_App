package djrAccounting.service.implementation;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.entity.ClientVendor;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientVendorServiceImplTest {
    @Mock
    ClientVendorRepository clientVendorRepository;
    @Mock
    MapperUtil mapperUtil;
    @Mock
    SecurityService securityService;
    @Mock
    InvoiceService invoiceService;
    @InjectMocks
    ClientVendorService clientVendorService;

    @Test
    void findById() {
        //Given
        when(clientVendorRepository.findById(anyLong())).thenReturn(Optional.of(new ClientVendor()));
        when(mapperUtil.convert(any(ClientVendor.class), new ClientVendorDto())).thenReturn(new ClientVendorDto());
        //When
        ClientVendorDto clientVendorDto=clientVendorService.findById(anyLong());
        //Then
        assertNotNull(ClientVendorDto.class);
    }

    @Test
    void listAllClientVendors() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void listClientsBySelectedUserCompany() {
    }

    @Test
    void listVendorsBySelectedUserCompany() {
    }

    @Test
    void duplicatedName() {
    }

    @Test
    void nameExists() {
    }
}