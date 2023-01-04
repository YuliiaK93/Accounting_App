package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.ReportingService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportingServiceImpl implements ReportingService {

    private final InvoiceProductService invoiceProductService;

    public ReportingServiceImpl(InvoiceProductService invoiceProductService) {
        this.invoiceProductService = invoiceProductService;
    }

    @Override
    public List<InvoiceProductDto> getAllInvoiceProductsThatApprovedFroCurrentCompany() {
        return invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany();
    }

    @Override
    public Map<String, BigDecimal> getAllProfitLossPerMonth() {
        return invoiceProductService.getAllByInvoiceStatusApprovedForCurrentCompany()
                .stream()
                .collect(Collectors.toMap(invoiceProductDto -> invoiceProductDto.getInvoice()
                        .getDate()
                        .getYear() + " " + invoiceProductDto.getInvoice()
                        .getDate()
                        .getMonth(), InvoiceProductDto::getProfitLoss, (BigDecimal::add)));
    }
}
