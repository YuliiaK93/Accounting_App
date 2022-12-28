package djrAccounting.service;

import djrAccounting.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany();
}
