package djrAccounting.service;

import djrAccounting.dto.ClientVendorDto;

import java.util.List;

public interface ClientVendorService {
    ClientVendorDto findById(Long id);

    List<ClientVendorDto> listAllClientVendors();

    void save(ClientVendorDto clientVendorDto);

    void update(ClientVendorDto clientVendorDto);

    void deleteById(Long id) throws IllegalAccessException;

    boolean duplicatedName(ClientVendorDto clientVendorDto);
    boolean nameExists(String name);

    List<ClientVendorDto> listClientsBySelectedUserCompany();

    List<ClientVendorDto> listVendorsBySelectedUserCompany();

    boolean hasRightToUpdate(Long id);
}
