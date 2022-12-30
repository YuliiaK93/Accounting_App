package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
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
    public List<InvoiceDto> findAllByCurrentUserCompany() {
        List<InvoiceDto> invoiceDtoList = invoiceRepository.findAllByCompanyId(securityService.getLoggedInUser()
                .getCompany().getId())
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
}
