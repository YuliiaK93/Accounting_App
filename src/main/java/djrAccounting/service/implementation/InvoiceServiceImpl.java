package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.common.UserPrincipal;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final MapperUtil mapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService, MapperUtil mapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.mapper = mapper;
    }

    @Override
    public List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany() {

        List<InvoiceDto> invoiceDtoList = invoiceRepository.getLast3ApprovedInvoicesByCompany(((UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()).getCompanyTitleForProfile())
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
        return mapper.convert(invoiceRepository.findById(id).orElseThrow(), InvoiceDto.class);
    }
}
