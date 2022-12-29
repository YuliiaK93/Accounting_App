package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.common.UserPrincipal;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final MapperUtil mapper;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapper) {
        this.invoiceRepository = invoiceRepository;
        this.mapper = mapper;
    }


    @Override
    public List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany() {

        return invoiceRepository.getLast3ApprovedInvoicesByCompany(((UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()).getCompanyTitleForProfile())
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDto findById(Long id) {
        return mapper.convert(invoiceRepository.findById(id).orElseThrow(), InvoiceDto.class);
    }

    @Override
    public List<InvoiceDto> getAllPurchaseInvoiceForCurrentCompany(){
        return invoiceRepository.findAllPurchaseInvoiceForCurrentCompany(((UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()).getCompanyTitleForProfile(), InvoiceType.PURCHASE)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

    }

}
