package djrAccounting.repository;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoice_InvoiceNoAndInvoice_Company_Title(String invoiceNo, String title);

    @Query(value = "SELECT i FROM InvoiceProduct i WHERE i.invoice.company.title = ?1 AND i.invoice.invoiceStatus = 'APPROVED' AND i.invoice.invoiceType = ?2")
    List<InvoiceProduct> findAllInvoicesByInvoice_Company_TitleAndInvoice_InvoiceStatusIsApproved(String companyTitle, InvoiceType invoiceType);
}