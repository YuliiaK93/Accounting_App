package djrAccounting.service;

import djrAccounting.dto.InvoiceDto;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany();

    InvoiceDto findById(Long id);

    boolean existsByClientVendorId(Long id);

    List<InvoiceDto> findSalesInvoicesByCurrentUserCompany();

    void save(InvoiceDto invoiceDto);

    String nextSalesInvoiceNo();

    List<InvoiceDto> getAllPurchaseInvoiceForCurrentCompany();
    String nextPurchaseInvoiceNo();

//    void deleteById(Long id);


    void approveInvoiceById(Long id);
}
