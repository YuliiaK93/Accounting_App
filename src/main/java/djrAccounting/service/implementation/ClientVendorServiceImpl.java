package djrAccounting.service.implementation;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.UserDto;
import djrAccounting.entity.ClientVendor;
import djrAccounting.entity.User;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.repository.UserRepository;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import djrAccounting.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientVendorServiceImpl implements ClientVendorService {

    private final ClientVendorRepository clientVendorRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final InvoiceService invoiceService;


    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, SecurityService securityService, InvoiceService invoiceService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.invoiceService = invoiceService;
    }

    @Override
    public ClientVendorDto findById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).orElseThrow(), ClientVendorDto.class);
    }

    @Override
    public List<ClientVendorDto> listAllClientVendors() {

        Long companyId=securityService.getLoggedInUser().getCompany().getId();
        return clientVendorRepository.findAll(Sort.by("clientVendorType")).stream()
                .filter(clientVendor -> clientVendor.getCompany().getId().equals(companyId))
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendorRepository.save(clientVendor);
    }

    @Override
    public void update(ClientVendorDto clientVendorDto) {
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(clientVendorDto.getId());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        if (clientVendor.isPresent()) {
            updatedClientVendor.setId(clientVendor.get().getId());
            clientVendorRepository.save(updatedClientVendor);
        }
    }

    @Override
    public void deleteById(Long id) throws IllegalAccessException {
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(id);
        if (clientVendor.isPresent()) {
            if(!(invoiceService.existsByClientVendorId(id))){
                clientVendor.get().setIsDeleted(true);
                clientVendorRepository.save(clientVendor.get());
            }else{
                throw new IllegalAccessException("Cannot be deleted. Has invoice linked to Client/Vendor");
            }
        }
        }

}


