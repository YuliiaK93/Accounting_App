package djrAccounting.controller;

import djrAccounting.dto.InvoiceDto;
import djrAccounting.enums.InvoiceStatus;
import djrAccounting.enums.InvoiceType;
import djrAccounting.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

@Controller
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {

    private final CompanyService companyService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final ClientVendorService clientVendorService;
    private final SecurityService securityService;

    public SalesInvoiceController(CompanyService companyService, InvoiceService invoiceService, InvoiceProductService invoiceProductService, ClientVendorService clientVendorService, SecurityService securityService) {
        this.companyService = companyService;
        this.invoiceService = invoiceService;
        this.invoiceProductService = invoiceProductService;
        this.clientVendorService = clientVendorService;
        this.securityService = securityService;
    }
    @GetMapping("/list")
    public String listInvoices(Model model){

        model.addAttribute("invoices",invoiceService.findSalesInvoicesByCurrentUserCompany());

    return "invoice/sales-invoice-list";
    }

    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {

        InvoiceDto invoiceDto = invoiceService.findById(id);

        model.addAttribute("company", companyService.findById(invoiceDto.getCompany().getId()));
        model.addAttribute("invoice", invoiceDto);
        model.addAttribute("invoiceProducts", invoiceProductService.findByInvoiceId(invoiceDto.getId()));

        return "invoice/invoice_print";
    }

    @GetMapping("/create")
    public String createSalesInvoicePage(Model model) {

        model.addAttribute("newSalesInvoice", new InvoiceDto());
        model.addAttribute("invoiceNo",invoiceService.nextSalesInvoiceNo());
        model.addAttribute("clients",clientVendorService.listClientsBySelectedUserCompany());
        model.addAttribute("date", LocalDate.now());


        return "invoice/sales-invoice-create";
    }


    @PostMapping("/create")
    public String createSalesInvoice(@Valid @ModelAttribute ("newSalesInvoice") InvoiceDto invoiceDto, BindingResult bindingResult){

        invoiceDto.setInvoiceType(InvoiceType.SALES);
        invoiceDto.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDto.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDto.setInvoiceNo(invoiceService.nextSalesInvoiceNo());
        invoiceDto.setDate(LocalDate.now());

        invoiceService.save(invoiceDto);

        return "invoice/sales-invoice-create";
    }

}
