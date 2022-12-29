package djrAccounting.service.implementation;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final SecurityService securityService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, SecurityService securityService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.securityService = securityService;
    }

    @Override
    public BigDecimal getTotalPriceByInvoice(String invoiceNo) {

        return invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, getCurrentCompanyTitle())
                .stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo) {
        return calculatePriceWithTax(invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, getCurrentCompanyTitle()));
    }

    @Override
    public BigDecimal getTotalCostForCurrentCompany() {

        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyTitle(), InvoiceType.PURCHASE));
    }

    @Override
    public BigDecimal getTotalSalesForCurrentCompany() {

        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyTitle(), InvoiceType.SALES));
    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {

        return list.stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .add(invoiceProduct.getPrice()
                                .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    private Long getCurrentCompanyTitle() {
        return securityService.getLoggedInUser().getCompany().getId();
    }
}
