package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.ReportingService;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
