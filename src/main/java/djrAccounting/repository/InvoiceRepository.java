package djrAccounting.repository;

import djrAccounting.entity.Invoice;
import djrAccounting.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT * FROM invoices WHERE invoice_status = 'APPROVED' AND company_id = (SELECT id FROM companies WHERE title = ?1) ORDER BY date DESC LIMIT 3", nativeQuery = true)
    List<Invoice> getLast3ApprovedInvoicesByCompanyId(Long companyId);

    @Query(value = "SELECT i FROM Invoice i WHERE i.company.title = ?1 AND i.invoiceType = ?2 order by i.invoiceNo desc ")
    List<Invoice> findAllPurchaseInvoiceForCurrentCompany(String companyTitle, InvoiceType invoiceType);


}

