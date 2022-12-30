package djrAccounting.repository;

import djrAccounting.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    @Query(value = "SELECT * FROM invoices WHERE invoice_status = 'APPROVED' AND company_id = ?1 ORDER BY date DESC LIMIT 3", nativeQuery = true)
    List<Invoice> getLast3ApprovedInvoicesByCompanyId(Long companyId);
    boolean existsByClientVendorId(Long id);
}

