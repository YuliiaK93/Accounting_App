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
    private final UserService userService;

    public ClientVendorServiceImpl(ClientVendorRepository clientVendorRepository, MapperUtil mapperUtil, UserService userService) {
        this.clientVendorRepository = clientVendorRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
    }

    @Override
    public ClientVendorDto findById(Long id) {
        return mapperUtil.convert(clientVendorRepository.findById(id).orElseThrow(), ClientVendorDto.class);
    }

    @Override
    public List<ClientVendorDto> listAllClientVendors() {
        //find out who is the user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDto userDto = userService.findByUsername(username);
        return clientVendorRepository.findAll(Sort.by("clientVendorType")).stream()
                .filter(clientVendor -> clientVendor.getCompany().getId().equals(userDto.getCompany().getId()))
                .map(clientVendor -> mapperUtil.convert(clientVendor, new ClientVendorDto()))
                .collect(Collectors.toList());
    }

    @Override
    public void save(ClientVendorDto clientVendorDto) {
        ClientVendor clientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        clientVendorRepository.save(clientVendor);
    }

    @Override// Double check if you should return object back?
    public void update(ClientVendorDto clientVendorDto) {
        //find the task in db
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(clientVendorDto.getId());
        ClientVendor updatedClientVendor = mapperUtil.convert(clientVendorDto, new ClientVendor());
        if (clientVendor.isPresent()) {
            updatedClientVendor.setId(clientVendorDto.getId());
            clientVendorRepository.save(updatedClientVendor);
        }
    }

    @Override
    public void deleteById(Long id) {
        Optional<ClientVendor> clientVendor = clientVendorRepository.findById(id);
        if (clientVendor.isPresent()) {
            //create boolean if invoice exists in invoice repository with this vendor id
            clientVendor.get().setIsDeleted(true);
            clientVendorRepository.save(clientVendor.get());
        }
        //in invoice repo, boolean findByClientVendorIdExists();
        //inject invoice service, use

    }
}


