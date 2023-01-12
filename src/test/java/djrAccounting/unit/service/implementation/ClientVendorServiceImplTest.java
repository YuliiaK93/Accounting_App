package djrAccounting.unit.service.implementation;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.ClientVendor;
import djrAccounting.entity.Company;
import djrAccounting.entity.User;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.implementation.ClientVendorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));

        ClientVendorDto expectedClientVendorDto = new ClientVendorDto();
        expectedClientVendorDto.setId(clientVendor.getId());

        when(mapperUtil.convert(clientVendor, ClientVendorDto.class)).thenReturn(expectedClientVendorDto);
        ClientVendorDto actualClientVendorDto = clientVendorServiceImpl.findById(1L);

        assertNotNull(actualClientVendorDto);
        assertEquals(expectedClientVendorDto, actualClientVendorDto);
    }

    @Test
    void deleteById_Valid_Test() throws IllegalAccessException {
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setIsDeleted(false);

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));
        when(invoiceService.existsByClientVendorId(1L)).thenReturn(false);

        clientVendorServiceImpl.deleteById(1L);

        verify(clientVendorRepository).findById(1L);
        verify(invoiceService).existsByClientVendorId(1L);
        verify(clientVendorRepository).save(clientVendor);
    }

    @Test
    public void deleteById_ClientVendorHasInvoices_Exception_Test() {
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setIsDeleted(false);

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));
        when(invoiceService.existsByClientVendorId(1L)).thenReturn(true);
        assertThrows(IllegalAccessException.class, () -> clientVendorServiceImpl.deleteById(1L));

        verify(clientVendorRepository).findById(1L);
        verify(invoiceService).existsByClientVendorId(1L);
    }

    @Test
    public void deleteById_ClientVendorNotFound_Test() throws IllegalAccessException {
        when(clientVendorRepository.findById(1L)).thenReturn(Optional.empty());
        clientVendorServiceImpl.deleteById(1L);
        verify(clientVendorRepository).findById(1L);
    }

    @Test
    void nameExists_Test() {
        when(clientVendorRepository.existsByClientVendorName(anyString())).thenReturn(true);
        assertTrue(clientVendorServiceImpl.nameExists(anyString()));
        verify(clientVendorRepository).existsByClientVendorName(anyString());
    }

    @Test
    void nameNotExists_Test() {
        when(clientVendorRepository.existsByClientVendorName(anyString())).thenReturn(false);
        assertFalse(clientVendorServiceImpl.nameExists(anyString()));
        verify(clientVendorRepository).existsByClientVendorName(anyString());
    }

    @Test
    void duplicatedName_Test() {
        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setClientVendorName("name");
        ClientVendorDto clientVendorDto = new ClientVendorDto();
        clientVendorDto.setId(1L);
        clientVendorDto.setClientVendorName("name");

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));

        boolean result = clientVendorServiceImpl.duplicatedName(clientVendorDto);
        assertFalse(result);
    }

    @Test
    void duplicatedName_DifferentName_ReturnsFalse_Test() {
        ClientVendorDto dto = new ClientVendorDto();
        dto.setId(1L);
        dto.setClientVendorName("name");

        ClientVendor cv = new ClientVendor();
        cv.setId(1L);
        cv.setClientVendorName("nickname");
        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(cv));
        when(clientVendorServiceImpl.nameExists("name")).thenReturn(false);

        boolean result = clientVendorServiceImpl.duplicatedName(dto);

        assertFalse(result);
    }

    @Test
    void duplicatedName_NameMatchFalse_NameExistsTrue_Test() {
        ClientVendorDto dto = new ClientVendorDto();
        dto.setId(1L);
        dto.setClientVendorName("name");
        ClientVendor cv = new ClientVendor();
        cv.setId(1L);
        cv.setClientVendorName("nickname");

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(cv));
        when(clientVendorServiceImpl.nameExists("name")).thenReturn(true);

        boolean result = clientVendorServiceImpl.duplicatedName(dto);

        assertTrue(result);
    }

    @Test
    void hasRightToUpdate_Valid_Test() {
        CompanyDto companyDto = new CompanyDto();
        companyDto.setId(1L);
        companyDto.setTitle("company");
        Company company = new Company();
        company.setId(1L);
        company.setTitle("company");
        UserDto userDto = new UserDto();
        userDto.setCompany(companyDto);

        when(securityService.getLoggedInUser()).thenReturn(userDto);

        ClientVendor clientVendor = new ClientVendor();
        clientVendor.setId(1L);
        clientVendor.setCompany(company);

        when(clientVendorRepository.findById(1L)).thenReturn(Optional.of(clientVendor));

        boolean result = clientVendorServiceImpl.hasRightToUpdate(1L);
        assertTrue(result);
    }
}