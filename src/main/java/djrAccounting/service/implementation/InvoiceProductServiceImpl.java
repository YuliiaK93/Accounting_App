package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {

    private final InvoiceProductRepository invoiceProductRepository;
    private final SecurityService securityService;
    private final MapperUtil mapper;
    private final InvoiceService invoiceService;


    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, SecurityService securityService, MapperUtil mapper, @Lazy InvoiceService invoiceService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.securityService = securityService;
        this.mapper = mapper;
        this.invoiceService = invoiceService;
    }

    @Override
    public BigDecimal getTotalPriceByInvoice(String invoiceNo) {
        return invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, getCurrentCompanyId())
                .stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    @Override
    public BigDecimal getTotalPriceWithTaxByInvoice(String invoiceNo) {
        return calculatePriceWithTax(invoiceProductRepository.findByInvoice_InvoiceNoAndInvoice_Company_Id(invoiceNo, getCurrentCompanyId()));
    }

    @Override
    public BigDecimal getTotalCostForCurrentCompany() {
        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyId(), InvoiceType.PURCHASE));
    }

    @Override
    public BigDecimal getTotalSalesForCurrentCompany() {
        return calculatePriceWithTax(invoiceProductRepository.findAllInvoicesByInvoice_Company_IdAndInvoice_InvoiceStatusIsApproved(getCurrentCompanyId(), InvoiceType.SALES));
    }

    @Override
    public BigDecimal getTotalProfitLossForCurrentCompany() {
        return invoiceProductRepository.findByInvoice_Company_Id(getCurrentCompanyId())
                .stream()
                .map(InvoiceProduct::getProfitLoss)
                .reduce(BigDecimal::add)
                .orElseThrow();
    }

    @Override
    public List<InvoiceProductDto> getAllByInvoiceStatusApprovedForCurrentCompany() {
        return invoiceProductRepository.findByInvoice_Company_IdAndInvoice_InvoiceStatusIsApprovedOrderByInvoice_DateDesc(getCurrentCompanyId())
                .stream()
                .map(invoiceProduct -> mapper.convert(invoiceProduct, InvoiceProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceProductDto findById(Long id) {
        return mapper.convert(invoiceProductRepository.findById(id).orElseThrow(), InvoiceProductDto.class);
    }

    @Override
    public List<InvoiceProductDto> findByInvoiceId(Long id) {
        return invoiceProductRepository.findByInvoiceId(id)
                .stream()
                .map(invoiceProduct -> mapper.convert(invoiceProduct, InvoiceProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void save(InvoiceProductDto invoiceProductDto, Long id) {

        invoiceProductDto.setProfitLoss(BigDecimal.ZERO); //todo profit loss calculation @mehmet
        invoiceProductDto.setInvoice(invoiceService.findById(id));
        InvoiceProduct invoiceProduct = mapper.convert(invoiceProductDto, InvoiceProduct.class);
        invoiceProductRepository.save(invoiceProduct);

    }

    private BigDecimal calculatePriceWithTax(List<InvoiceProduct> list) {
        return list.stream()
                .map(invoiceProduct -> invoiceProduct.getPrice()
                        .add(invoiceProduct.getPrice()
                                .multiply(BigDecimal.valueOf(invoiceProduct.getTax()))
                                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_EVEN))
                        .multiply(BigDecimal.valueOf(invoiceProduct.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private Long getCurrentCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }
}
