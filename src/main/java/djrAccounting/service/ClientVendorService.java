package djrAccounting.service;

import djrAccounting.dto.ClientVendorDto;
import djrAccounting.enums.ClientVendorType;

public interface ClientVendorService {

    ClientVendorDto findById(Long id);
}
