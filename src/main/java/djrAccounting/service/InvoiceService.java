package djrAccounting.service;

import djrAccounting.dto.InvoiceDto;

public interface InvoiceService {

    InvoiceDto findById(Long id);

}
