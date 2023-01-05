package djrAccounting.service;

import djrAccounting.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    BigDecimal getTotalPriceByInvoice(String invoiceNo);

    BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo);

    BigDecimal getTotalCostForCurrentCompany();

    BigDecimal getTotalSalesForCurrentCompany();

    BigDecimal getTotalProfitLossForCurrentCompany();

    List<InvoiceProductDto> getAllByInvoiceStatusApprovedForCurrentCompany();

    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> findByInvoiceId(Long id);

    void save(InvoiceProductDto invoiceProductDto, Long id);

    void deleteInvoiceProductById(Long id);

    void removeInvoiceProduct(Long invoiceProductId);
}
