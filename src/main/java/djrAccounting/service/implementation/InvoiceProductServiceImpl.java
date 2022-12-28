package djrAccounting.service.implementation;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.entity.common.UserPrincipal;
import djrAccounting.enums.InvoiceType;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceProductService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository) {
        this.invoiceProductRepository = invoiceProductRepository;
    }

    @Override
    public BigDecimal getTotalPriceByInvoice(String invoiceNo) {

        return invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Title(invoiceNo, getCurrentCompanyTitle())
                .stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo) {


        BigDecimal price = calculatePriceWithTax(invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Title(invoiceNo, getCurrentCompanyTitle()));

        System.out.println(price);
        return price;
    }

    @Override
    public BigDecimal getTotalCostForCurrentCompany() {

        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_TitleAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyTitle(), InvoiceType.PURCHASE));
    }

    @Override
    public BigDecimal getTotalSalesForCurrentCompany() {

        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_TitleAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyTitle(), InvoiceType.SALES));
    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {

        return list.stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .add(invoiceProduct.getPrice()
                                .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                                .divide(BigDecimal.valueOf(100), RoundingMode.FLOOR))
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    private String getCurrentCompanyTitle() {

        return ((UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()).getCompanyTitleForProfile();
    }
}
