package djrAccounting.service.implementation;


import djrAccounting.dto.InvoiceDto;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;

    private final MapperUtil mapper;


    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, MapperUtil mapper) {
        this.invoiceRepository = invoiceRepository;
        this.mapper = mapper;
    }

    @Override
    public InvoiceDto findById(Long id) {
        return mapper.convert(invoiceRepository.findById(id).orElseThrow(), InvoiceDto.class);
    }
}
