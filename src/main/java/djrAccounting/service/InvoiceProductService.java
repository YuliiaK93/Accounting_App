package djrAccounting.service;

import java.math.BigDecimal;

public interface InvoiceProductService {
    BigDecimal getTotalPriceByInvoice(String invoiceNo);

    BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo);

    BigDecimal getTotalCostForCurrentCompany();

    BigDecimal getTotalSalesForCurrentCompany();
}
