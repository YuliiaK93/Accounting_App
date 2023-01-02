package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.Invoice;
import djrAccounting.enums.CompanyStatus;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.entity.common.UserPrincipal;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;
    private final MapperUtil mapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService, SecurityService securityService, MapperUtil mapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
        this.mapper = mapper;
    }

    @Override
    public List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany() {
        List<InvoiceDto> invoiceDtoList = invoiceRepository.getLast3ApprovedInvoicesByCompanyId(securityService.getLoggedInUser()
                        .getCompany()
                        .getId())
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

        invoiceDtoList.forEach(invoiceDto -> {
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
        });

        return invoiceDtoList;
    }

    @Override
    public InvoiceDto findById(Long id) {
        InvoiceDto invoiceDto = mapper.convert(invoiceRepository.findById(id).orElseThrow(), InvoiceDto.class);
        invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getInvoiceNo()));
        invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getInvoiceNo()));
        invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));

        return invoiceDto;
    }

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByClientVendorId(id);
    }

    @Override
    public List<InvoiceDto> findSalesInvoicesByCurrentUserCompany() {
        List<InvoiceDto> invoiceDtoList = invoiceRepository.findAllByCompanyIdAndInvoiceType(securityService.getLoggedInUser()
                .getCompany().getId(), InvoiceType.SALES)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

        invoiceDtoList.forEach(invoiceDto -> {
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
        });

        return invoiceDtoList;
    }

    @Override
    public void save(InvoiceDto invoiceDto) {
        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        Invoice invoice = mapper.convert(invoiceDto, Invoice.class);
        invoiceRepository.save(invoice);
    }

    @Override
    public String nextSalesInvoiceNo() {
        Invoice invoice = invoiceRepository.findTopByCompanyIdOrderByIdDesc(securityService.getLoggedInUser().getCompany().getId());
        String invoiceNo = invoice.getInvoiceNo();
        String substring = invoiceNo.substring(2);
        int number = Integer.parseInt(substring) +1;
        if(number<10) {return "S-"+"00"+number;}
        else if (number<100) {return "S-"+"0"+number;}

        return  "S-"+number;
    }

    @Override
    public List<InvoiceDto> getAllPurchaseInvoiceForCurrentCompany() {
        List<InvoiceDto> purchaseInvoice = invoiceRepository.findAllPurchaseInvoiceForCurrentCompany(((UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()).getCompanyTitleForProfile(), InvoiceType.PURCHASE)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

       purchaseInvoice.

        // get all the products for each invoice
        // than multiply product quantity x product price
        // than sum all of than to find the total price of each invoice
        // when iterate over each product we need keep the tax rate for calculate the tax value
        // we need to find total coast of each invoice by add tax value to price for each product and
        // than update price and total tax values of each invoiceDTO

    }

}
