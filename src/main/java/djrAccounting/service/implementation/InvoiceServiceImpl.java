package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.Invoice;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
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
        if (invoiceDto.getClientVendor().getClientVendorType().equals(ClientVendorType.CLIENT)) {
            invoiceDto.setInvoiceType(InvoiceType.SALES);
        } else {
            invoiceDto.setInvoiceType(InvoiceType.PURCHASE);
        }

        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        Invoice invoice = mapper.convert(invoiceDto, Invoice.class);
        invoiceRepository.save(invoice);
        invoiceDto.setId(invoice.getId());
    }

    @Override
    public String nextSalesInvoiceNo() {
        Invoice invoice = invoiceRepository.findTopByCompanyIdOrderByIdDesc(securityService.getLoggedInUser().getCompany().getId());
        String invoiceNo = invoice.getInvoiceNo();
        String substring = invoiceNo.substring(2);
        int number = Integer.parseInt(substring) + 1;
        if (number < 10) {
            return "S-" + "00" + number;
        } else if (number < 100) {
            return "S-" + "0" + number;
        }

        return "S-" + number;
    }

    @Override
    public List<InvoiceDto> getAllPurchaseInvoiceForCurrentCompany() {
        List<InvoiceDto> purchaseInvoiceList = invoiceRepository.findAllByCompanyIdAndInvoiceType(securityService.getLoggedInUser()
                        .getCompany().getId(), InvoiceType.PURCHASE)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

        purchaseInvoiceList.forEach(invoiceDto -> {
            invoiceDto.setInvoiceProducts(invoiceProductService.findByInvoiceId(invoiceDto.getId()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getInvoiceNo()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
        });

        return purchaseInvoiceList;
    }

    @Override
    public String nextPurchaseInvoiceNo() {//get the Last one created invoice for the company
        Invoice invoice = invoiceRepository.findTopByCompanyIdOrderByIdDesc(securityService.getLoggedInUser().getCompany().getId());
        String invoiceNo = invoice.getInvoiceNo();//get number
        String substring = invoiceNo.substring(2);
        int number = Integer.parseInt(substring) + 1;
        if (number < 10) {
            return "P-" + "00" + number;
        } else if (number < 100) {
            return "P-" + "0" + number;
        }
        return "P-" + number;
    }
}
