package djrAccounting.service;

import djrAccounting.dto.InvoiceProductDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ReportingService {
    List<InvoiceProductDto> getAllInvoiceProductsThatApprovedFroCurrentCompany();

    Map<String, BigDecimal> getAllProfitLossPerMonth();
}