package djrAccounting.service;

import djrAccounting.dto.CompanyDto;
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


}
