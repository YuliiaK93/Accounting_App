package djrAccounting.service.implementation;

import djrAccounting.dto.ClientVendorDto;

import djrAccounting.entity.ClientVendor;
import djrAccounting.entity.Company;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
       return clientVendorRepository.findByCompany_IdOrderByClientVendorTypeAscClientVendorNameAsc(companyId)
               .stream()
               .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
               .collect(Collectors.toList());

    }

    @Override
    public void save(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendor.setCompany(mapperUtil.convert(securityService.getLoggedInUser().getCompany(), Company.class));
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
            if (!(invoiceService.existsByClientVendorId(id))) {
                clientVendor.get().setIsDeleted(true);
                clientVendorRepository.save(clientVendor.get());
            } else {
                throw new IllegalAccessException("Cannot be deleted. Has invoice linked to Client/Vendor");
            }
        }
    }

    @Override
    public List<ClientVendorDto> listClientsBySelectedUserCompany() {
        return clientVendorRepository.findAllByCompanyIdAndClientVendorTypeOrderByClientVendorName(securityService.getLoggedInUser()
                        .getCompany().getId(), ClientVendorType.CLIENT)
                .stream()
                .map(client -> mapperUtil.convert(client, ClientVendorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClientVendorDto> listVendorsBySelectedUserCompany() {
        return  clientVendorRepository.findAllByCompanyIdAndClientVendorTypeOrderByClientVendorName(securityService.getLoggedInUser()
                        .getCompany().getId(), ClientVendorType.VENDOR)
                .stream()
                .map(client -> mapperUtil.convert(client, ClientVendorDto.class))
                .collect(Collectors.toList());
    }


    @Override
    public boolean nameExists(String name) {
        return clientVendorRepository.existsByClientVendorName(name);
    }
}


