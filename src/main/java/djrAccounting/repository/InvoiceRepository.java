package djrAccounting.repository;

import djrAccounting.entity.Company;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query(value = "SELECT * FROM invoices WHERE invoice_status = 'APPROVED' AND company_id = ?1 ORDER BY date DESC LIMIT 3", nativeQuery = true)
    List<Invoice> getLast3ApprovedInvoicesByCompanyId(Long companyId);

    boolean existsByClientVendorId(Long id);

    List<Invoice> findAllByCompanyId(Long id);

    List<Invoice> findAllByCompanyIdAndInvoiceType(Long id, InvoiceType invoiceType);

    Invoice findTopByCompanyIdOrderByIdDesc(Long id);

    List<Invoice> findAllByCompanyIdAndInvoiceTypeOrderByLastUpdateDateTimeDesc(Long companyId, InvoiceType invoiceType);


}


