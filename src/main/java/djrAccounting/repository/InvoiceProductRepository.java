package djrAccounting.repository;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoice_Company_Id(Long id);

    List<InvoiceProduct> findByInvoice_IdAndInvoice_Company_Id(Long invoiceId, Long companyId);

    @Query(value = "SELECT i FROM InvoiceProduct i WHERE i.invoice.company.id = ?1 AND i.invoice.invoiceStatus = 'APPROVED' AND i.invoice.invoiceType = ?2")
    List<InvoiceProduct> findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(Long companyId, InvoiceType invoiceType);

    @Query("select i from InvoiceProduct i " +
            "where i.invoice.company.id = :id and i.invoice.invoiceStatus = 'APPROVED' " +
            "order by i.invoice.date DESC")
    List<InvoiceProduct> findByInvoice_Company_IdAndInvoice_InvoiceStatusIsApprovedOrderByInvoice_DateDesc(@Param("id") Long companyId);

    List<InvoiceProduct> findByInvoiceId(Long id);

    @Query("SELECT i FROM InvoiceProduct i " +
            "WHERE i.remainingQuantity > 0 AND i.invoice.invoiceType = 'PURCHASE' AND i.product.id = ?1 AND i.invoice.invoiceStatus = 'APPROVED' " +
            "ORDER BY i.invoice.lastUpdateDateTime")
    List<InvoiceProduct> findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(Long productId);


}