package djrAccounting.service;

import djrAccounting.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    BigDecimal getTotalPriceByInvoice(Long invoiceId);

    BigDecimal getTotalPriceWithTaxByInvoice(Long invoiceId);

    BigDecimal getTotalCostForCurrentCompany();

    BigDecimal getTotalSalesForCurrentCompany();

    BigDecimal getTotalProfitLossForCurrentCompany();

    List<InvoiceProductDto> getAllByInvoiceStatusApprovedForCurrentCompany();

    InvoiceProductDto findById(Long id);

    List<InvoiceProductDto> findByInvoiceId(Long id);

    InvoiceProductDto save(InvoiceProductDto invoiceProductDto, Long id);

    void deleteInvoiceProductById(Long id);

    void removeInvoiceProduct(Long invoiceProductId);

}
