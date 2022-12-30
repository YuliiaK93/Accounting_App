package djrAccounting.repository;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoice_Company_Id(Long id);
    List<InvoiceProduct> findByInvoice_InvoiceNoAndInvoice_Company_Id(String invoiceNo, Long companyId);

    @Query(value = "SELECT i FROM InvoiceProduct i WHERE i.invoice.company.id = ?1 AND i.invoice.invoiceStatus = 'APPROVED' AND i.invoice.invoiceType = ?2")
    List<InvoiceProduct> findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(Long companyId, InvoiceType invoiceType);

    @Query("select i from InvoiceProduct i " +
            "where i.invoice.company.id = :id and i.invoice.invoiceStatus = 'APPROVED' " +
            "order by i.invoice.date DESC")
    List<InvoiceProduct> findByInvoice_Company_IdAndInvoice_InvoiceStatusIsApprovedOrderByInvoice_DateDesc(@Param("id") Long companyId);

    List<InvoiceProduct> findByInvoiceId(Long id);
}