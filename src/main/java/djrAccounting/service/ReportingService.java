package djrAccounting.service;

import djrAccounting.dto.InvoiceProductDto;

import java.util.List;

public interface ReportingService {
    List<InvoiceProductDto> getAllInvoiceProductsThatApprovedFroCurrentCompany();
}