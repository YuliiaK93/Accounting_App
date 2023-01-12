package djrAccounting.service.implementation;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.dto.InvoiceProductDto;
import djrAccounting.dto.ProductDto;
import djrAccounting.entity.Invoice;
import djrAccounting.entity.InvoiceProduct;
import djrAccounting.enums.ClientVendorType;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.mapper.MapperUtil;
import djrAccounting.repository.InvoiceProductRepository;
import djrAccounting.repository.InvoiceRepository;
import djrAccounting.service.InvoiceProductService;
import djrAccounting.service.InvoiceService;
import djrAccounting.service.ProductService;
import djrAccounting.service.SecurityService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;
    private final MapperUtil mapper;
    private final InvoiceProductRepository invoiceProductRepository;
    private final ProductService productService;

    public InvoiceServiceImpl(InvoiceRepository invoiceRepository, InvoiceProductService invoiceProductService, SecurityService securityService, MapperUtil mapper, InvoiceProductRepository invoiceProductRepository, ProductService productService) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceProductService = invoiceProductService;
        this.securityService = securityService;
        this.mapper = mapper;
        this.invoiceProductRepository = invoiceProductRepository;
        this.productService = productService;
    }

    @Override
    public List<InvoiceDto> getLast3ApprovedInvoicesForCurrentUserCompany() {

        return priceSetter(dtoMapper(invoiceRepository.getLast3ApprovedInvoicesByCompanyId(getLoggedInCompanyId())));
    }

    @Override
    public InvoiceDto findById(Long id) {
        InvoiceDto invoiceDto = mapper.convert(invoiceRepository.findById(id).orElseThrow(), InvoiceDto.class);
        invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getId()));
        invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getId()));
        invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));

        return invoiceDto;
    }

    @Override
    public boolean existsByClientVendorId(Long id) {
        return invoiceRepository.existsByClientVendorId(id);
    }

    @Override
    public List<InvoiceDto> findSalesInvoicesByCurrentUserCompany() {
        List<InvoiceDto> invoiceDtoList = invoiceRepository.findAllByCompanyIdAndInvoiceTypeOrderByLastUpdateDateTimeDesc(securityService.getLoggedInUser()
                        .getCompany().getId(), InvoiceType.SALES)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

        invoiceDtoList.forEach(invoiceDto -> {
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getId()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getId()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
        });

        return invoiceDtoList;
    }

    @Override
    public InvoiceDto save(InvoiceDto invoiceDto) {

        if (invoiceDto.getClientVendor().getClientVendorType().equals(ClientVendorType.CLIENT)) {

            invoiceDto.setInvoiceType(InvoiceType.SALES);

        } else invoiceDto.setInvoiceType(InvoiceType.PURCHASE);

        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());

        return mapper.convert(invoiceRepository.save(mapper.convert(invoiceDto, Invoice.class)), InvoiceDto.class);
    }

    @Override
    public String nextSalesInvoiceNo() {
        Invoice invoice = invoiceRepository.findTopByCompanyIdOrderByIdDesc(securityService.getLoggedInUser()
                .getCompany()
                .getId());
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
        List<InvoiceDto> purchaseInvoiceList = invoiceRepository.findAllByCompanyIdAndInvoiceTypeOrderByLastUpdateDateTimeDesc(securityService.getLoggedInUser()
                        .getCompany().getId(), InvoiceType.PURCHASE)
                .stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());

        purchaseInvoiceList.forEach(invoiceDto -> {
            invoiceDto.setInvoiceProducts(invoiceProductService.findByInvoiceId(invoiceDto.getId()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getId()));
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getId()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
        });

        return purchaseInvoiceList;
    }

    @Override
    public String nextPurchaseInvoiceNo() {
        Invoice invoice = invoiceRepository.findTopByCompanyIdOrderByIdDesc(securityService.getLoggedInUser()
                .getCompany()
                .getId());
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
        List<InvoiceProduct> invoiceProductList = invoice.getInvoiceProducts();
        invoiceProductList.forEach(salesInvoiceProduct -> {
            List<InvoiceProduct> purchaseInvoiceProducts = invoiceProductRepository.findByRemainingQuantityGreaterThanAndInvoice_InvoiceTypeAndProduct_IdOrderByLastUpdateDateTimeAsc(salesInvoiceProduct.getProduct().getId());
            int quantity = salesInvoiceProduct.getQuantity();
            int invoiceProductIndex = 0;
            BigDecimal totalCost = BigDecimal.ZERO;
            while (quantity > 0) {
                InvoiceProduct currentInvoiceProduct = purchaseInvoiceProducts.get(invoiceProductIndex);
                int remainingInvoiceQuantity = quantity - currentInvoiceProduct.getRemainingQuantity();
                if (remainingInvoiceQuantity > 0) {
                    totalCost = totalCost.add(getTotalWithTax(currentInvoiceProduct, currentInvoiceProduct.getRemainingQuantity()));
                    invoiceProductIndex++;
                    quantity -= currentInvoiceProduct.getRemainingQuantity();
                    currentInvoiceProduct.setRemainingQuantity(0);
                } else {
                    totalCost = totalCost.add(getTotalWithTax(currentInvoiceProduct, quantity));
                    int quantityBeforeReduction = quantity;
                    quantity -= currentInvoiceProduct.getRemainingQuantity();
                    currentInvoiceProduct.setRemainingQuantity(currentInvoiceProduct.getRemainingQuantity() - quantityBeforeReduction);
                }
                invoiceProductRepository.save(currentInvoiceProduct);
            }
            salesInvoiceProduct.setProfitLoss(getTotalWithTax(salesInvoiceProduct, salesInvoiceProduct.getQuantity()).subtract(totalCost));
            invoiceProductRepository.save(salesInvoiceProduct);
            productService.decreaseQuantityInStock(salesInvoiceProduct.getProduct()
                    .getId(), salesInvoiceProduct.getQuantity());
        });

        invoice.setInvoiceStatus(InvoiceStatus.APPROVED); //todo ask kicchi if we need a transaction here. what if changing the status to approved will fail but all quantitites has already been changed
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);
    }

    private BigDecimal getTotalWithTax(InvoiceProduct invoiceProduct, int remainingQuantity) {
        BigDecimal beforeTax = BigDecimal.valueOf(remainingQuantity).multiply(invoiceProduct.getPrice());
        BigDecimal taxValue = BigDecimal.valueOf(invoiceProduct.getTax()).multiply(beforeTax).divide(BigDecimal.valueOf(100L));
        return beforeTax.add(taxValue);
    }

    @Override
    public void deleteInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
        invoiceProductService.findByInvoiceId(id)
                .forEach(invoiceProductDto ->
                        invoiceProductService.deleteInvoiceProductById(invoiceProductDto.getId()));
    }

    @Override
    public void deletePurchaseInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).get();
        List<InvoiceProductDto> invoiceProductList = invoiceProductService.findByInvoiceId(id);
        invoiceProductList.stream()
                .map(invoiceProductDto -> mapper.convert(invoiceProductDto, InvoiceProduct.class))
                .forEach(invoiceProduct -> invoiceProduct.setIsDeleted(true));
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    public void approvePurchaseInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow();
        List<InvoiceProduct> invoiceProductList = invoice.getInvoiceProducts();
        boolean productReadyToOrder = invoiceProductList.size() > 0;
        if (isAuthoredToApproveInvoice() && productReadyToOrder) {
            for (InvoiceProduct invoiceProduct : invoiceProductList) {
                ProductDto productDto = productService.findById(invoiceProduct.getProduct().getId());
                productDto.setQuantityInStock(productDto.getQuantityInStock() + invoiceProduct.getQuantity());
                productService.save(productDto);
            }
            invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
            invoice.setDate(LocalDate.now());
            invoiceRepository.save(invoice);
        }
    }

    private boolean isAuthoredToApproveInvoice() {
        String loggedInUserRole = securityService.getLoggedInUser().getRole().getDescription();
        return loggedInUserRole.equals("Manager");
    }

    private Long getLoggedInCompanyId() {
        return securityService.getLoggedInUser().getCompany().getId();
    }

    private List<InvoiceDto> priceSetter(List<InvoiceDto> invoiceDtoList) {

        invoiceDtoList.forEach(invoiceDto -> {
            invoiceDto.setPrice(invoiceProductService.getTotalPriceByInvoice(invoiceDto.getId()));
            invoiceDto.setTotal(invoiceProductService.getTotalPriceWithTaxByInvoice(invoiceDto.getId()));
            invoiceDto.setTax(invoiceDto.getTotal().subtract(invoiceDto.getPrice()));
            invoiceDto.setInvoiceProducts(invoiceProductService.findByInvoiceId(invoiceDto.getId()));
        });

        return invoiceDtoList;
    }

    private List<InvoiceDto> dtoMapper(List<Invoice> clientVendors) {

        return clientVendors.stream()
                .map(invoice -> mapper.convert(invoice, InvoiceDto.class))
                .collect(Collectors.toList());
    }
}
