package djrAccounting.service.implementation;

import djrAccounting.entity.InvoiceProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    List<InvoiceProduct> findByInvoice_InvoiceNo(String invoiceNo);
}