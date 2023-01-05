package djrAccounting.service.implementation;

import djrAccounting.dto.CompanyDto;
import djrAccounting.dto.InvoiceDto;
import djrAccounting.entity.Company;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.entity.Product;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.repository.ProductRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;
    private final MapperUtil mapper;
    private final ProductRepository productRepository;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService, SecurityService securityService, MapperUtil mapper, ProductRepository productRepository) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
        this.mapper = mapper;
        this.productRepository = productRepository;

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

    @Override
    public void approveInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        CompanyDto companyDto = securityService.getLoggedInUser().getCompany();
        Company company = mapper.convert(companyDto, Company.class);
//- all stock quantities of items that are purchased in the invoice should be decreased by the amount on the invoice"
 //       Map<InvoiceProduct, Integer> productMap = new LinkedHashMap<>();
        List<InvoiceProduct> invoiceProductList = invoice.getInvoiceProducts();
 //       List<Product> productList = productRepository.findAllByCategoryCompany(company);
        List<Invoice> purchaseInvoiceList = new LinkedList<>();



        invoiceProductList.forEach(invoiceProduct -> {

            purchaseInvoiceList.addAll(invoiceRepository.findAllByInvoiceProductsContainingAndInvoiceTypeAndCompanyOrderById(invoiceProduct,InvoiceType.PURCHASE, company));
            int temp = invoiceProduct.getProduct().getQuantityInStock();
            invoiceProduct.getProduct().setQuantityInStock(temp-invoiceProduct.getQuantity());
            decreaseRemainingQuantityOfInvoices(purchaseInvoiceList, invoiceProduct);

        });
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED); //Transaction???
        invoice.setDate(LocalDate.now());
// - profit/loss should be calculated for all sales invoice products and saved

    }

    private void decreaseRemainingQuantityOfInvoices(List<Invoice> purchaseInvoiceList, InvoiceProduct saleInvoiceProduct) {

        purchaseInvoiceList.forEach(purchaseInvoice -> {
            purchaseInvoice.getInvoiceProducts().forEach(purchaseInvoiceProduct -> {
                while (purchaseInvoiceProduct.equals(saleInvoiceProduct) && purchaseInvoiceProduct.getRemainingQuantity()>0){
                    if (saleInvoiceProduct.getQuantity() >= purchaseInvoiceProduct.getRemainingQuantity()){
                        int saleTempQuantity = saleInvoiceProduct.getQuantity();
                        int purchaseTempQuantity= purchaseInvoiceProduct.getRemainingQuantity();
                        purchaseInvoiceProduct.setRemainingQuantity(0);
                        saleInvoiceProduct.setQuantity(saleTempQuantity-purchaseTempQuantity);
                    } else{
                        int purchaseTempQuantity = purchaseInvoiceProduct.getRemainingQuantity();
                        int saleTempQuantity = saleInvoiceProduct.getQuantity();
                        purchaseInvoiceProduct.setRemainingQuantity(purchaseTempQuantity-saleTempQuantity);
                        saleInvoiceProduct.setRemainingQuantity(0);
                    }
                }
            });
        });
    }
}
