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

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    ClientVendorServiceImpl clientVendorServiceImpl;

    @Test
    void findById_validInput_Test() {
        //Given Client vendor in DB with a particular ID
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        //When search in DB get ID you get Optional of clientVendor
        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));
        //You expect the return of method to bring dto with same id
        ClientVendorDto expectedClientVendorDto = new ClientVendorDto();
        expectedClientVendorDto.setId(clientVendor.getId());
       // convert clientVendor to Dto, and get expected clientVendor
        when(mapperUtil.convert(clientVendor, ClientVendorDto.class)).thenReturn(expectedClientVendorDto);
        //Possibility for the output
        ClientVendorDto actualClientVendorDto = clientVendorServiceImpl.findById(1L);
        // Assert
        assertNotNull(actualClientVendorDto);//returns and object
        assertEquals(expectedClientVendorDto, actualClientVendorDto);//the Object is exactly the object we need
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