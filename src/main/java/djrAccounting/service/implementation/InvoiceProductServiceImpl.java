package djrAccounting.service.implementation;

import djrAccounting.entity.InvoiceProduct;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository) {
        this.invoiceProductRepository = invoiceProductRepository;
    }

    @Override
    public BigDecimal getTotalPriceByInvoice(String invoiceNo) {

        return invoiceProductRepository.findByInvoice_InvoiceNo(invoiceNo)
                .stream()
                .map(InvoiceProduct::getPrice)
                .reduce(BigDecimal::add).orElseThrow();
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo) {

        return invoiceProductRepository.findByInvoice_InvoiceNo(invoiceNo)
                .stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getTax() / 100)))
                .reduce(BigDecimal::add).orElseThrow();
    }
}
