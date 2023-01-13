package djrAccounting.service;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.InvoiceProductDto;

import java.util.List;

public interface InvoiceService {
    List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany();

    InvoiceDto findById(Long id);

    boolean existsByClientVendorId(Long id);

    List<InvoiceDto> findSalesInvoicesByCurrentUserCompany();

    InvoiceDto save(InvoiceDto invoiceDto);

    String nextSalesInvoiceNo();

    List<InvoiceDto> getAllPurchaseInvoiceForCurrentCompany();

    String nextPurchaseInvoiceNo();

    void approveInvoiceById(Long id);

    void deleteInvoiceById(Long id);

    void deletePurchaseInvoiceById(Long id);
    void approvePurchaseInvoice(Long id);

   void update(InvoiceDto invoiceDto);

}
