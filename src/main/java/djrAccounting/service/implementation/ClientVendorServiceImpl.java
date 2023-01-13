package djrAccounting.service.implementation;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.entity.ClientVendor;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.exception.ClientVendorNotFoundException;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.ClientVendorRepository;
import djrAccounting.service.ClientVendorService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;

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
        return mapperUtil.convert(clientVendorRepository.findById(id).orElseThrow(()->new ClientVendorNotFoundException("Client/Vendor with indicated ID is not found")), ClientVendorDto.class);
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
    public ClientVendorDto save(ClientVendorDto clientVendorDto) {
        clientVendorDto.setCompany(securityService.getLoggedInUser().getCompany());
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        return mapperUtil.convert(clientVendorRepository.save(clientVendor), ClientVendorDto.class);
    }

    @Override
    public ClientVendorDto update(ClientVendorDto clientVendorDto) {
        clientVendorRepository.findById(clientVendorDto.getId()).orElseThrow();
        return mapperUtil.convert(clientVendorRepository.save(mapperUtil.convert(clientVendorDto, ClientVendor.class)), ClientVendorDto.class);
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
        return clientVendorRepository.findAllByCompanyIdAndClientVendorTypeOrderByClientVendorName(securityService.getLoggedInUser()
                        .getCompany().getId(), ClientVendorType.VENDOR)
                .stream()
                .map(client -> mapperUtil.convert(client, ClientVendorDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasRightToUpdate(Long id) {
        String loggedInUserCompany = securityService.getLoggedInUser().getCompany().getTitle();
        ClientVendor clientVendor = clientVendorRepository.findById(id).orElseThrow(()->new ClientVendorNotFoundException("Client/Vendor with indicated ID is not found"));
        return clientVendor.getCompany().getTitle().equals(loggedInUserCompany);
    }

    @Override
    public boolean duplicatedName(ClientVendorDto clientVendorDto) {
        boolean nameMatch = clientVendorRepository.findById(clientVendorDto.getId()).orElseThrow(()->new ClientVendorNotFoundException("Client/Vendor with indicated ID is not found")).getClientVendorName().equals(clientVendorDto.getClientVendorName());
        return !nameMatch && nameExists(clientVendorDto.getClientVendorName());
    }

    @Override
    public boolean nameExists(String name) {
        return clientVendorRepository.existsByClientVendorName(name);
    }


}


